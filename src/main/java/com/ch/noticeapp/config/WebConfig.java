package com.ch.noticeapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration //이 클래스의 목적은 로직이 아닌 설정에 있다...
public class WebConfig implements WebMvcConfigurer {

    /*
        /api/ 로 들어오는 요청 중
        http://localhost:7777 에서 온 요청은
        GET/POST/PUT/DELETE 를 허용해라
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                //nginx로 운영중인 클라이언트를 추가
                .allowedOrigins("http://localhost:7777")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

}




