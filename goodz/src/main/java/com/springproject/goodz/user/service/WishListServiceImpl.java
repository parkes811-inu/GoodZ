package com.springproject.goodz.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.user.dto.Wish;
import com.springproject.goodz.user.mapper.WishListMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WishListServiceImpl implements WishListService {
    
    @Autowired
    private WishListMapper wishListMapper;


    // 저장 체크 여부 - id 기준
    @Override
    public boolean listById(Wish wish) throws Exception {
        int result = wishListMapper.listById(wish);

        boolean ischecked = false; // 체크여부 off -> false / on -> true

        if (result == 0) {
            return ischecked;
        }

        return !ischecked;
    }

    // 저장 갯수 갱신
    @Override
    public int countWish(Wish wish) throws Exception {
        int result = wishListMapper.countWish(wish);
    
            return result;
    }

    // 저장 off -> on
    @Override
    public int wishOn(Wish wish) throws Exception {
        int result = wishListMapper.wishOn(wish);

        return result;
    }

    // 저장 on -> off
    @Override
    public int wishOff(Wish wish) throws Exception {
        int result = wishListMapper.wishOff(wish);

        return result;
    }


    
}
