package com.springproject.goodz.admin.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AdminService {

    // 유저가 판매한 상품 단일 조회
    public Map<String, Object> userSale(int sNo) throws Exception;

        // 유저가 판매한 상품 상태 변경
    public void updateUserSaleState(@Param("sNo") int sNo, @Param("saleState") String saleState) throws Exception;
}
