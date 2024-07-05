package com.springproject.goodz.pay.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.dto.Sales;
import com.springproject.goodz.pay.mapper.PayMapper;
import com.springproject.goodz.product.dto.ProductOption;

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

    // 유저가 미결제시 취소하면 상태 취소로 변경 
    @Override
    public void cancelPurchase(int purchaseNo) throws Exception {
        payMapper.cancelPurchase(purchaseNo);
    }
    
    // 유저별 판매 내역 조회
    @Override
    public List<Sales> findSalesByUserId(@Param("userId") String userId) throws Exception {
        return payMapper.findSalesByUserId(userId);
    }

    @Override
    public List<Purchase> findPurchasesByUserIdWithPage(String userId, int start, int rows) throws Exception {
        return payMapper.findPurchasesByUserIdWithPage(userId, start, rows);
    }



    @Override
    public ProductOption selectProductWithOption(int pNo, int optionId) throws Exception {
        return payMapper.selectProductWithOption(pNo, optionId);
    }
}
