package com.springproject.goodz.pay.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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

    @Override
    public int updatePurchase(Purchase purchase) throws Exception {
        return payMapper.updatePurchase(purchase);
    }

    @Override
    public Purchase selectPurchase(int purchaseNo) throws Exception {
        return payMapper.selectPurchase(purchaseNo);
    }

    // 유저별 구매 내역 조회
    @Override
    public List<Purchase> findPurchasesByUserId(@Param("userId") String userId) throws Exception {
        return payMapper.findPurchasesByUserId(userId);
    }
    
    // 유저별 판매 내역 조회
    @Override
    public List<Sales> findSalesByUserId(@Param("userId") String userId) throws Exception {
        return payMapper.findSalesByUserId(userId);
    }
}
