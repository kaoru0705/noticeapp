package com.ch.noticeapp.notice.controller;

import com.ch.noticeapp.notice.dto.mybatis.Notice;
import com.ch.noticeapp.notice.service.MybatisNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/*공지게시판 요청을 처리하는 컨트롤러*/
@Slf4j
@RestController //이것을 쓰면 @ResponseBody를 명시할 필요가 없다
//@RequestMapping("/api/notices")
public class MybatisNoticeController {

    private final MybatisNoticeService noticeService;

    //반드시 MybatisNoticeService의 인스턴스가 존재해야, 이 컨트롤러의 인스턴스도 생성되게 강제하기 위해..
    // MybatisNoticeService에서의 Spring IoC Container가 알아서 주입해준다.
    //결론: 실수 방지
    public MybatisNoticeController(MybatisNoticeService noticeService){
        this.noticeService=noticeService;
    }

    // 내부에서는 사실상
    // (GET,    /api/notices/{noticeId}) → getContent()
    // (DELETE, /api/notices/{noticeId}) → delete()
    // (PUT,    /api/notices/{noticeId}) → update()

    //글쓰기 요청 처리
    @PostMapping
    //@ResponseBody //jackson이  java클래스를 --> JSON 문자열로 바꿔줌
    public ResponseEntity<Notice> regist(@RequestBody Notice notice){ //jackson이  JSON 문자열 ->  java클래스로..
        log.debug("넘겨받은 제목은 {}", notice.getTitle());

        Notice created=noticeService.regist(notice);

        //created() 메서드 안에는  클라이언트에게 등록된 자원의 위치를 알려주는 코드를 작성할 수 있음
        // 왜 쓰지? 상태 코드 201 반환(요청대로 새로운 자원이 성공적으로 생성되었다는 뜻) ok는 상태코드 200
        // URI.create("/api/notices/" + created.getNoticeId()) 이 부분은 Location 이라는 항목에 주소를 담아 보낸다.
        // window.location.href = res.headers.get("Location") 따라서 받은 쪽에서 이게 가능
        //return ResponseEntity.created(URI.create("/api/notices/"+notice.getNoticeId())).body(notice);

        return ResponseEntity.created(URI.create("/api/notices/"+created.getNoticeId())).body(created);
    }

    //글삭제
    @DeleteMapping("/{noticeId}") //변수가 경로의 일부로 포함될 경우 {중괄호}를 붙이고, @PathVariable
                                                                            //사용하면, 경로의 일부로 보는 것이 아니라, 변수로 인식
    public ResponseEntity<Void> delete( @PathVariable Long noticeId){
        log.debug("삭제할 글의 pk는 {}",  noticeId);
        noticeService.delete(noticeId);

        // noContent() 204 상태 코드
        // 204 No Content를 쓸 경우: "성공했어! 근데 지웠으니까 줄 데이터는 없어. 본문은 비어있으니 찾지 마!"
        return ResponseEntity.noContent().build();
    }

    //글 1건 가져오기
    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getContent(@PathVariable Long noticeId){
        log.debug("noticeId is {}", noticeId);
        return ResponseEntity.ok(noticeService.getContent(noticeId));
    }

    //글목록 가져오기
    @GetMapping
    public ResponseEntity<List<Notice>> getList(){
        return ResponseEntity.ok(noticeService.getList());
    }

    //글한건 수정 요청 처리
    @PutMapping("/{noticeId}")
    public ResponseEntity<Notice> update(@PathVariable Long noticeId,@RequestBody Notice notice){
        log.debug("notice is {}", notice);
        notice.setNoticeId(noticeId);//pk값 대입

        return ResponseEntity.ok(noticeService.update(notice));
    }
}