package com.ch.noticeapp.notice.exception;

//개발자가 예외처리를 하지 않아도 컴파일이 가능한 예외는 RuntimeException 이라 함
public class NoticeException extends RuntimeException{
    //미리 만들어놓은 예외 코드 객체 사용하기
    private final NoticeErrorCode errorCode;//에러 메시지 뿐만 아니라, 에러 코드도 이미 포함

    public NoticeException(NoticeErrorCode errorCode){
        super(errorCode.getMessage()); //한글 메시지가 부모 예외 객체로 전달
        this.errorCode = errorCode;
    }
    public NoticeErrorCode getErrorCode(){
        return errorCode;
    }
}
