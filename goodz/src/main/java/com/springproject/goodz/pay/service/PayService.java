package com.springproject.goodz.pay.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.mapper.PayMapper;

public interface PayService {
    
    

    public void savePurchase(Purchase purchase) throws Exception;
}
