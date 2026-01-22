package com.ch.noticeapp.notice.service;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.entity.Notice;
import com.ch.noticeapp.notice.exception.NoticeErrorCode;
import com.ch.noticeapp.notice.exception.NoticeException;
import com.ch.noticeapp.notice.repository.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JpaNoticeService {

    // 쿼리의 즉시 반영등을 처리할 수 있다.
    @PersistenceContext
    private EntityManager em;

    // Jpa는 JpaRepository 인터페이스를 통해 Entity를 제어한다. 또한 Entity가 제어되면
    // 자동으로 CRUD가 수행. 따라서 개발자가 SQL문을 볼 일이 없다.
    private final NoticeRepository noticeRepository;
    /* 아래의 생성자는 Lombok의 @RequiredArgsContructor로 대체
    public JpaNoticeService(NoticeRepository noticeRepository){
        this.noticeRepository = noticeRepository;
    }
    */

    // 글 등록
    @Transactional
    public ResponseNotice regist(RequestNotice requestNotice){
        Notice notice = new Notice(requestNotice.getTitle(), requestNotice.getWriter(), requestNotice.getContent());

        /*
            save() 메서드에 의해 데이터베이스에 즉시 쿼리문이 실행되는 것은 아님
            notice를 영속성(Persistence) 컨텍스트 관리 대상으로 등록한다는 의미(JPA에게 맡김)
            INSERT 발생하는 시점은 현재 메서드가 완료되면서 트랜잭션이 확정될 때이다.
            따라서 등록 뿐만 아니라 JPA는 모든 메소드에 @Transactional 필수

         */
        // 이 시점에 이미 noticeId가 채워짐
        // 엥 메서드가 완료되면서 일어나는 거 아닌가? Notice Entity의 @GeneratedValue에 의해 ID를 알기 위해 INSERT SQL을 즉시 실행해야 함
        Notice saved = noticeRepository.save(notice);  // db에 반영 후 저장된 게시물 반환
        //System.out.println("saved regdate = " + saved.getRegdate());
        em.refresh(saved);  // flush 발생 -> INSERT 실행 -> SELECT 실행하여 DB값 재로딩
        //System.out.println("saved regdate after refresh = " + saved.getRegdate());
        // 주의할 점!!! 절대로 응답에 사용될 객체로 Notice Entity를 사용해서는 안 된다.. 왜??
        // 데이터베이스와 관련된 정보, 객체간의 관계(erd 상 관계... 등등)
        // 따라서, Entity 안에 들어있는 데이터 중 필요한 것만 꺼내서 담을 만한 그릇 DTO가 필요함

        //ResponseNotice obj = new ResponseNotice(saved.getTitle(), saved.getWrtier(), );
        // 특정 객체안의 데이터를 다른 데이터를 옮길 때 생성자를 통해 옮기면 매개변수의 순서가 중요하므로
        // 매개 변수의 수가 많을 때는 코드가 복답하고, 처리하기 힘들다..
        // 빌더패턴(GOF)
        return ResponseNotice.from(notice);
    }

    // 글 목록 가져오기
    @Transactional(readOnly = true)   // readOnly의 의미
    // 이 메서드는 엔티티를 수정하지 않을 것이라는 의도를 스프링에 명확히 하기 위함임
    // 따라서 JAP가 엔티티에 대한 변경 감지와 같은 불필요한 작업을 하지 않도록, 즉 최적화시키기 위함
    public List<ResponseNotice> getList() {
        // findAll()로 가져온 결과를 우리가 원하는 형태인 List<ResponseNotice>로 변환 작업을 해야 함...
        // 전통적인 방법으로는 for()문 돌려서 요소를 List에 add()..  js(배열메서드) : java(Stream)
        // Notice entity에 있는 noticeId를 사용해야 한다. db 칼럼명인 notice_id는 안 됨
        // List<Notice> notices = noticeRepository.findAll(); 이건 미친 짓이다.
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeId"))       // 모든 레코드 가져오기
                .stream()       // 이 시점부터 컬렉션을 선언적 방식으로 처리할 수 있는 스트림을 시작함
                .map(ResponseNotice :: from).toList();
    }

    // 글 한 건 가져오기
    @Transactional
    public ResponseNotice getDetail(Long noticeId){
        /*
            findById()에 의해 Optional 이 반환됨
            Optional이란? 데이터가 들어있을 수도 있고, 들어있지 않을 수도 있음을 객체로 표현한 것
            orElseThrow() 메서드란? 데이터가 들어있으면 꺼내고, 없으면 예외를 발생시켜준다.
         */

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(()-> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        notice.increaseHit();   // 조회수 1 증가

        return ResponseNotice.from(notice);     // 입력 순서 맞출 필요 없음
    }

    // 한 건 수정
    @Transactional
    public ResponseNotice update(Long noticeId, RequestNotice request){

        // 수정에 앞서서 넘겨받은 파라미터에 대항하는 객체가 존재하는지 여부.. 확인
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(()->new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        // 수정하기 ? SQL을 직접 건드리는 것이 아니라, 개발자가 Notice 엔티티를 수정하면, JPA가 이 메서드 완료시점에
        // MySQL에 변경된 내용을 반영해줌.. 즉, 개발자는 객체에 집중하라!!
        notice.update(request.getTitle(), request.getWriter(), request.getContent());

        return ResponseNotice.from(notice);
    }

    // 한 건 삭제
    @Transactional
    public void delete(Long noticeId){

        // 삭제하기 전에 정말로 존재하는 레코드인지 확인
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(()->new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        noticeRepository.delete(notice);    // 삭제 요청
    }
}