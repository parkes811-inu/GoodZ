package com.springproject.goodz.admin.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AdminService {

    // 유저가 판매한 상품 단일 조회
    Map<String, Object> userSale(int saleNo) throws Exception;

    // 유저가 판매한 상품 상태 변경
    void updateUserSaleState(int sNo, String saleState) throws Exception;

    // 판매 완료 시 상품 옵션의 재고 수량 증가
    void incrementStockQuantity(int pNo, String size) throws Exception;

    // 정산 완료에서 다른 상태로 변경 시 상품 옵션의 재고 수량 감소
    void decrementStockQuantity(int pNo, String size) throws Exception;
}
