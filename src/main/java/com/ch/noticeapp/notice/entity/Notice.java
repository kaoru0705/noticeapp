package com.ch.noticeapp.notice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Mybatis는 자바객체와 개발자가 정의해놓은 SQL문을 자동으로 매핑해주는 SQLMapper라 한다면
 * JPA(Java Persistence API) 는 자바객체와 데이터베이스 테이블간 매핑을 자동으로 해주는 기술
 *                                           (Object Relation Mapping)
 * */
@Entity     // 이 객체가 일반 클래스가 JPA 기술이 적용된 클래스임을 명시,
// 개발자가 쿼리문을 작성하는 것이 아니라, 이 객체에 데이터를 채워넣기만 하면, 알아서 내부적으로 insert 동작
// OOP 개발자가 쿼리문 신경쓰지 말고, 객체간의 관계 등에 집중하라는 의미
@Table(name = "notice")   // 이 객체와 매핑될 테이블을 명시
// JPA는 반드시 parameter 없는 constructor가 존재해야 함. 단, 엔티티 객체는 개발자가 임의로 인스턴스를 생성해서는 안되고
// JPA 내부적으로 알아서 생성해주므로, 개발자에 의한 생성자 호출은 금지시켜야 한다.. 따라서 public이 아닌, protected로
// 선언해야 한다.. protected로 선언하면, JPA 및 상속관계에 있는 객체들이 접근할 수 있게 됨...
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // protected Notice(){}와 동일
@Getter     // @Setter를 추가하지 않는 이유, 아무래도 DB와 연결되어 있으므로, 함부로 변경할 수 있는 setter 등을 제공하지 않아야 함
// DTO와는 차원이 다르고, 목적도 다르다... 절대로 Entity를 클라이언트가 전송하는 parameter를 받는 용도로 사용하지
// 말아야 한다. ( 컨트롤러 계층에서는 사용하면 안 됨.. 모델 영역에서만 사용해야 함)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;  // db의 컬럼이 _언더바로 되어 있어도 엔티티는 camelCase로 작성

    // 굳이 데이터베이스까지 깊숙히 들어가서 에러를 일으키면 시간, 자원 낭비
    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "writer", length = 20)
    private String writer;

    @Lob    // Large Object 의 약자로서, 부여한 이유?
    // @Lob를 String에 붙이면 CLOB, byte[]에 붙이면 BLOB 계열로 매핑됨
    // MySQL의 경우 String을 보통 기본으로 varchar(255)로 가려는 경향이 있으므로, columnDefinition="text"
    // 를 명시함으로써, 컬럼의 타입을 TEXT 정확히 알려주기 위함, 현재 우리는 이미 mysql에 text로 되어 있기 때문에
    // 개발에 큰 이득은 없지만, 다른 개발자와 프로젝트를 공유할 때, 이 속성을 보고 용량이 큰 스트림 데이터임을 문서화시키는
    // 이득은 있음
    @Column(name = "content", columnDefinition = "text")
    private String content;

    // String이 아닌 날짜 자료형으로 처리했을 때 장점?
    // 정렬, 날짜 비교, 기간 조회 명확, 유효한 날짜만 허용, 문자열 오염 방지
    // 날짜는 기본으로 설정되므로 insertable, updatable  false
    @Column(name = "regdate", insertable = false, updatable = false)
    private LocalDateTime regdate;

    @Column(name="hit")
    private int hit;

    // 개발자가 추후 데이터 1건을 담기 위해 사용될 생성자 정의
    public Notice(String title, String writer, String content){
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public void update(String title, String writer, String content){
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public void increaseHit(){
        hit += 1;
    }

}
