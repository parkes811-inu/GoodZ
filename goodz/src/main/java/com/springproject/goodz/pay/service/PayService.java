package com.springproject.goodz.pay.service;

import com.springproject.goodz.pay.dto.Sales;

public interface PayService {

    public int savePurchase(Purchase purchase) throws Exception;
    
    // 판매
    public int insertSale(Sales sales) throws Exception;
    

}
