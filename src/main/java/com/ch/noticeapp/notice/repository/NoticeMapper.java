package com.ch.noticeapp.notice.repository;

import com.ch.noticeapp.notice.dto.mybatis.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//Mapper 인터페이스를 사용하면, 굳이 DAO를 별도로
//만들어서 SqlSessionTemplate을 사용할 필요조차 없음
@Mapper
public interface NoticeMapper {
    //어차피 인터페이스에 선언된 메서드들은 디폴트가 public이다
    int insert(Notice notice); //한건 등록
    List<Notice> findAll(); //목록 가져오기
    Notice findById(long noticeId);
    int update(Notice notice); //한건 수정
    int deleteById(long noticeId); //한건 삭제
    int increaseHit(long noticeId);//조회수 증가


}
