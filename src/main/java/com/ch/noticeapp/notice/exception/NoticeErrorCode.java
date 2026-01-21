package com.ch.noticeapp.notice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NoticeErrorCode {
    //enum 안에는 문자,숫자,논리값(기본자료형) 뿐 아니라 객체자료형도 모아놓을 수 다
    //주문 , 입금확인, 출고중, 배송중, 배송완료
    NOTICE_NOT_FOUND("해당 글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    NOTICE_CREATE_FAIL("글 등록에 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    NOTICE_UPDATE_FAIL("글 수정에 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    NOTICE_DELETE_FAIL("글 삭제에 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    NoticeErrorCode(String message,HttpStatus status ){
        this.message=message;
        this.status=status;
    }

    //json 응답이나, 로그 등에 사용할 에러코드 문자열
    public String getCode(){
        return this.name(); //NOTICE_NOT_FOUND, NOTICE_CREATE_FAIL ...
    }

}
