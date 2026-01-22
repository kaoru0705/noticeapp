package com.ch.noticeapp.notice.controller;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.service.JpaNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 목록 요청 처리
    @GetMapping
    public ResponseEntity<List<ResponseNotice>> getList(){

        return ResponseEntity.ok(jpaNoticeService.getList());
    }

    // 수정 요청 처리
    @PutMapping("/{noticeId}")
    public ResponseEntity<ResponseNotice> update(@PathVariable Long noticeId, @RequestBody RequestNotice request){

        return ResponseEntity.ok(jpaNoticeService.update(noticeId, request));
    }

    // 삭제 요청 처리
    @DeleteMapping("/{noticeId}")
    // ResponseBody로 JSON으로 바꿀 때 아무 것도 없는 것도 객체로 보내야 한다.
    public ResponseEntity<Void>  delete(@PathVariable Long noticeId) {

        jpaNoticeService.delete(noticeId);

        return ResponseEntity.noContent().build();  // 204 noContent()
    }
}