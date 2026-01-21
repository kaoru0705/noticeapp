package com.ch.noticeapp.notice.service;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.entity.Notice;
import com.ch.noticeapp.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JpaNoticeService {
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
        Notice saved = noticeRepository.save(notice);  // db에 반영

        // 주의할 점!!! 절대로 응답에 사용될 객체로 Notice Entity를 사용해서는 안 된다.. 왜??
        // 데이터베이스와 관련된 정보, 객체간의 관계(erd 상 관계... 등등)
        // 따라서, Entity 안에 들어있는 데이터 중 필요한 것만 꺼내서 담을 만한 그릇 DTO가 필요함

        //ResponseNotice obj = new ResponseNotice(saved.getTitle(), saved.getWrtier(), );
        // 특정 객체안의 데이터를 다른 데이터를 옮길 때 생성자를 통해 옮기면 매개변수의 순서가 중요하므로
        // 매개 변수의 수가 많을 때는 코드가 복답하고, 처리하기 힘들다..
        // 빌더패턴(GOF)
        return ResponseNotice.from(notice);
    }
}
