package com.ch.noticeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  //@Configuration+ @EnableAutoConfiguration + @ComponentScan
//@EnableWebMvc - 스프링 mvc에서 필요햇지만 부트에서 명시하면 오히려 충돌 남
public class NoticeappApplication {
    //Spring IoC Container(ApplicationContext) 생성
    //모든 설정 클래스 읽기
    //컴포넌트 스캔 시작
    public static void main(String[] args) {
        SpringApplication.run(NoticeappApplication.class, args);
    }

}
