package com.springproject.goodz.pay.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.dto.Sales;

public interface PayService {

    // 구매 조회
    public Purchase selectPurchase(int purchaseNo) throws Exception;

    // 구매 등록
    public int savePurchase(Purchase purchase) throws Exception;
    
    // 구매 업데이트
    public int updatePurchase(Purchase purchase) throws Exception;
    
    // 판매
    public int insertSale(Sales sales) throws Exception;
    
    // 유저별 구매 내역 조회
    public List<Purchase> findPurchasesByUserId(@Param("userId") String userId) throws Exception;

    // 유저별 판매 내역 조회
    public List<Sales> findSalesByUserId(@Param("userId") String userId) throws Exception;
    
}
