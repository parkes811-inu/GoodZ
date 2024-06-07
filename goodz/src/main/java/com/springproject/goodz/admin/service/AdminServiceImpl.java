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
        adminMapper.updateUserSaleState(sNo, saleState);
    }
    
}
