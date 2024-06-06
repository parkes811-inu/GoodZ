package com.springproject.goodz.pay.service;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.dto.Sales;

public interface PayService {

    // 구매 등록
    public int savePurchase(Purchase purchase) throws Exception;
    
    // 구매 업데이트
    public int updatePurchase(Purchase purchase) throws Exception;
    
    // 판매
    public int insertSale(Sales sales) throws Exception;
    

}
