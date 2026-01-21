package com.ch.noticeapp.notice.dto.mybatis;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/*
@Data는 많은 어노테이션을 자동으로 붙여주므로, 필요한것만 설정하자
@Getter + @Setter + @ToString + @EnqualsAndHashCode + @RequiredArgsConstructor
*/
@Getter
@Setter
public class Notice {
    private Long noticeId; //데이터가 존재하지 않을 경우엔 null 이 들어있으므로, 데이터 없을을 판단하기에 유리..
                                    //long 은 0이 들어가있으므로, 데이터 없음 보다는 의미가 약함..
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regdate; //단순한 문자열로 날짜 자료형을 처리하면, 불러오기만 할때는 별문제 없지만
                                                //날짜 비교, 올바른 형식에 맞지 않음을 평가하는데 있어서는 불리함..
    private int hit;
}
