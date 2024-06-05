package com.springproject.goodz.user.service;

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
    
    
}
