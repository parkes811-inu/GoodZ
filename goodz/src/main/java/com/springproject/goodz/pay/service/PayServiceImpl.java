package com.springproject.goodz.pay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.dto.Sales;
import com.springproject.goodz.pay.mapper.PayMapper;

@Service
public class PayServiceImpl implements PayService {
    
    @Autowired
    private PayMapper payMapper;


    @Override
    public int savePurchase(Purchase purchase) throws Exception {
        return payMapper.insertPurchase(purchase);
    }

    public int insertSale(Sales sales) throws Exception {
        return payMapper.insertSale(sales);
    }
}
