package com.ch.noticeapp.notice.controller;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.service.JpaNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     // 모든 요청 처리 메서드에 @Responsebody 를 붙일 필요가 없다.
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class JpaNoticeController {

    private final JpaNoticeService jpaNoticeService;

    @PostMapping
    // 클라이언트가 전송한 파라미터가 json 문자열이므로, json 문자열을 자바 객체와 자동으로 매핑을 시켜주는 jackson을 사용하려면
    // @RequestBody를 사용해야 한다.
    public ResponseEntity<ResponseNotice> regist(@RequestBody RequestNotice request){
        ResponseNotice created = jpaNoticeService.regist(request);

        return ResponseEntity.ok(created);
    }


}