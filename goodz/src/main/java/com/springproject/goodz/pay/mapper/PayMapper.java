package com.springproject.goodz.pay.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.pay.dto.Purchase;

@Mapper
public interface PayMapper {
    
    // 구매 등록
    public void insertPurchase(Purchase purchase) throws Exception;
}
