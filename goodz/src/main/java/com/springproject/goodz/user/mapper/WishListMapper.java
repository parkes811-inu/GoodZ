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
    
    // 관심에 담긴 상품 번호 리스트 반환
    public List<Integer> listNumByUserId (String userId) throws Exception;
    
}
