package com.ch.noticeapp.notice.dto.response;

import com.ch.noticeapp.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/*
    JPA기반으로 개발을 하게 될 경우 테이블과 1:1 매핑되는 Entity 클래스가 필요하고,
    이 클래스는 DTO가 아니다, 하지만 이미 우리가 Notice.java 라는 이름으로 평상 시 DTO로 써먹는 이름을
    Entity가 점유하였으므로, 응답용 DTO를 정의 하자.
 */
@Builder
@Getter
public class ResponseNotice {
    private Long noticeId;
    private String title;
    private String writer;
    private String content;
    private Integer hit;
    private LocalDateTime regdate;

    /*
    *   빌더 패턴을 사용하지 않을 경우, Notice 안에 있는 데이터를 생성자를 이용하여 옮길 때
    *   코드가 매우 복잡해짐 (순서를 맞춰야 하니깐..)
    *   new ResponseNotice(인수1, 인수2, 인수3)
    * */
    // 빌더 패턴으로 메서드 정의할 예정...
    public static ResponseNotice from(Notice notice) {
        return ResponseNotice.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .writer(notice.getWriter())
                .content(notice.getContent())
                .regdate(notice.getRegdate())
                .hit(notice.getHit())
                .build();
    }

}
