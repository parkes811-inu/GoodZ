package com.springproject.goodz.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.user.dto.Wish;


@Mapper
public interface WishListMapper {

    // 저장 체크 여부 - id 기준
    public int listById(Wish wish) throws Exception;

    // 저장 갯수 가져오기
    public int countWish(Wish wish) throws Exception;

    // 저장 off -> on
    public int wishOn (Wish wish) throws Exception;

    // 저장 on -> off
    public int wishOff (Wish wish) throws Exception;

    // 종속된 저장들 모두 삭제
    public int deleteAll(Wish wish) throws Exception;
    

    // 유저 관심리스트 조회 - parentTable, userId 기준 조회
    public List<Wish> listByParent (Wish wish) throws Exception;
    
}
