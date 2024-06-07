package com.springproject.goodz.admin.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.admin.mapper.AdminMapper;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminMapper adminMapper;
    
    // 유저가 판매한 상품 단일 조회
    @Override
    public Map<String, Object> userSale(int sNo) throws Exception {
        return adminMapper.userSale(sNo);
    }

    // 유저가 판매한 상품 상태 변경
    @Override
    public void updateUserSaleState(int sNo, String saleState) throws Exception {
        Map<String, Object> saleDetail = adminMapper.userSale(sNo);
        String currentSaleState = (String) saleDetail.get("saleState");

        if ("completed".equals(currentSaleState) && !"completed".equals(saleState)) {
            adminMapper.decrementStockQuantity((int) saleDetail.get("productNo"), (String) saleDetail.get("size"));
        } else if (!"completed".equals(currentSaleState) && "completed".equals(saleState)) {
            adminMapper.incrementStockQuantity((int) saleDetail.get("productNo"), (String) saleDetail.get("size"));
        }

        adminMapper.updateUserSaleState(sNo, saleState);
    }

    // 판매 완료 시 상품 옵션의 재고 수량 증가
    @Override
    public void incrementStockQuantity(int pNo, String size) throws Exception {
        adminMapper.incrementStockQuantity(pNo, size);
    }

    // 정산 완료에서 다른 상태로 변경 시 상품 옵션의 재고 수량 감소
    @Override
    public void decrementStockQuantity(int pNo, String size) throws Exception {
        adminMapper.decrementStockQuantity(pNo, size);
    }
    
}
