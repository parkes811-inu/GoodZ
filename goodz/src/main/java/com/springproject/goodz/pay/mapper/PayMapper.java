package com.springproject.goodz.pay.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.dto.Sales;

@Mapper
public interface PayMapper {
    
    // 구매 등록
    public int insertPurchase(Purchase purchase) throws Exception;

    // 구매 업데이트
    public int updatePurchse(Purchase purchase) throws Exception;

    // 판매
    public int insertSale(Sales sales) throws Exception;

}
