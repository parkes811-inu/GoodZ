package com.springproject.goodz.user.service;

import java.util.List;

import com.springproject.goodz.user.dto.Wish;

public interface WishListService {

    // 저장 체크 여부 - id 기준
    public boolean listById(Wish wish) throws Exception;

    // 저장 갯수 가져오기
    public int countWish(Wish wish) throws Exception;

    // 저장 off -> on
    public int wishOn (Wish wish) throws Exception;

    // 저장 on -> off
    public int wishOff (Wish wish) throws Exception;
    
    // 관심에 담긴 상품 번호 리스트 반환
    public List<Integer> listNumByUserId (String userId) throws Exception;

    // 종속된 저장들 모두 삭제
    public int deleteAll(Wish wish) throws Exception;

    // 유저 관심리스트 조회 - parentTable, userId 기준 조회
    public List<Wish> listByParent (Wish wish) throws Exception;
    
}
