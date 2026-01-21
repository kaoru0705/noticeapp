package com.ch.noticeapp.notice.service;

import com.ch.noticeapp.notice.dto.mybatis.Notice;
import com.ch.noticeapp.notice.exception.NoticeErrorCode;
import com.ch.noticeapp.notice.exception.NoticeException;
import com.ch.noticeapp.notice.repository.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MybatisNoticeService {

    private final NoticeMapper noticeMapper;
    /*스프링부트에서는 @Autowire 보다는 생성자 주입 방식을 더 권고함. 더 선호함
    *  이유? 서비스 객체가 반드시 필요로 하는 객체에 대한 주입을 강제함으로써 , 실수 방지하기 위함
    * */
    public MybatisNoticeService(NoticeMapper noticeMapper){
        this.noticeMapper=noticeMapper;
    }

    //글 등록
    //이 메서드 안에서 throw 로 인한 , 메서드 정의부에 throws처리를 과연 해야 하는가?
    //해도 되고, 안해도 된다..
    //할 경우 - 메서드의 정의부가 너무 지저분해지고, 이 서비스의 상위 객체인 인터페이스를 정의할 경우 , 해당 메서드에도
    //throws를 명시해야 하므로, 코드량이 많아짐
    //안할경우 - 대부분의 개발자들은 throw를 만나면 반드시 throws를 해야한다고 알고잇으나, 사실상 자바 내부적으로는
    //throw가 발생해도 , 프로그램이 종료되지 않고 예외를 메서드 호출한 호출부로 전달해줌 .
    //throw 를 명시했을때 throws를 해야하는 경우는 예전부터 작성해오던 SQLException 에서나 사용했던 방법이므로,
    //우리의 경우 RuntimeException을 상속받은 예외 객체를 사용하고 있으므로, 굳이 throws를 명시할 필요 없다..
    //즉 SQLException 의 경우는 처리를 강요하는 강제Exception이엇기 때문에 throws가 필요했던 거였음..
    public Notice regist(Notice notice) {
        //insert 후 반환되는 숫자는 레코드가 반영되었는지 여부를 판단할때 사용하고,
        //그 외의 문법 상오류, db의 문제로 인한 외부적 에러는 예외발생으로 판단해야 함..
        try {
            int affected = noticeMapper.insert(notice);

            if (affected != 1) {
                throw new NoticeException(NoticeErrorCode.NOTICE_CREATE_FAIL);
            }
            return noticeMapper.findById(notice.getNoticeId()); //이 시점의  즉 insert가 완료된 시점의 notice DTO에는 useGeneratedKeys 속성에 의해 pk값이
            //채워진 상태이므로, 어떤 글을 넣었는지에 대한 정보를 클라이언트에게 전송할 수 있다..
        }catch (NoticeException e){
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw new NoticeException(NoticeErrorCode.NOTICE_CREATE_FAIL);
        }
    }

    //삭제 처리
    public void delete(long noticeId) {
        try {
            int affeced = noticeMapper.deleteById(noticeId);
            if(affeced !=1){//반영이 안되었다면..일종의 예외로 처리하자
                throw new NoticeException(NoticeErrorCode.NOTICE_DELETE_FAIL);
            }
        } catch (NoticeException e) {
            //이 catch영역은 필수는 아니다. 단지 개발자가 예외와 관련되어 전달 하는것 이외에 무언가 더 하고싶 다면
            //이 영역을 이용하기 위해 작성함..
            e.printStackTrace();
            throw e;
        }catch(Exception e){//여기에 올수있는 예의 종류가 너무 많고, 예측 불가하므로 Exception형으로 받아야 하며,
            // 글삭제하는데 방해되는 모든 예외는
            //모두 NoticeException으로 몰아서 처리해버리자
            throw new NoticeException(NoticeErrorCode.NOTICE_DELETE_FAIL);
        }
    }

    //한건 가져오기
    public Notice getContent(long noticeId){
        try {
            Notice notice = noticeMapper.findById(noticeId);
            if (notice == null) {
                throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
            }
            return notice;
        }catch(Exception e){
            e.printStackTrace();//개발자를 위해 로그 남기기...
            throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }
    }

    //목록 가져오기
    public List<Notice> getList(){
        try {
            return noticeMapper.findAll();
        } catch (Exception e) {
            throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }
    }

    //한건 수정하기
    public Notice update(Notice notice){
        //바로 수정하지 말고, 넘겨받은 정보를 이용하여 정말로 그 대상이 존재하는지 체크
        try {
            Notice found = noticeMapper.findById(notice.getNoticeId());
            if (found == null) {//글이 존재하지 않는다면, 수정은 실패로 처리 ...
                throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
            }

            //위의 예외를 만나지 않았다면 수정을 시도
            int affected = noticeMapper.update(notice);
            if (affected != 1) {
                throw new NoticeException(NoticeErrorCode.NOTICE_UPDATE_FAIL);
            }
            return noticeMapper.findById(notice.getNoticeId());
        }catch(Exception e){
            e.printStackTrace();// 개발자가 원인 분석을 하기 위해 기록 남기기..
            throw new NoticeException(NoticeErrorCode.NOTICE_UPDATE_FAIL);
        }
    }
}




















