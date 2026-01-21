package com.ch.noticeapp.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/*
    JPA 이용 시 Setter를 명시하지 않아도 됨
    - JPA는 자바의 리플렉션의 원리를 이용함
        Field field = NoticeRequest.class.getDeclaredField("title");
        field.set("title"); // 주입...
 */
@Getter
public class RequestNotice {

    /*
        https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
        @NotBlank - null, 빈 문자열 "", 공백만 있는 문자열 " " 허용하지 않음, db까지 가기 전에 요청 검증 단계에서 유효성 체크
        @Size - 문자열, 컬렉션, 배열의 길이를 검증. 즉, String의 경우 문자열 길이에 대해 기준 초과 여부를 판단해주는 검증 어노테이션
    */
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;
}
