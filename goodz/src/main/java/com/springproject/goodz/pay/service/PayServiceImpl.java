package com.springproject.goodz.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.mapper.PayMapper;

@Service
public class PayServiceImpl implements PayService{
    
    @Autowired
    private PayMapper payMapper;


    @Override
    public void savePurchase(Purchase purchase) throws Exception {
        payMapper.insertPurchase(purchase);
    }
}
