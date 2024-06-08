package com.springproject.goodz.admin.service;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // 유저가 판매한 상품 전체 조회
    List<Map<String, Object>> userSaleList() throws Exception;

    // 상태별 판매 현황 카운트
    List<Map<String, Object>> countUserSalesByState() throws Exception;

    // 유저가 판매한 상품 단일 조회
    Map<String, Object> userSale(int saleNo) throws Exception;

    // 유저가 판매한 상품 상태 변경
    void updateUserSaleState(int sNo, String saleState) throws Exception;

    // 판매 완료 시 상품 옵션의 재고 수량 증가
    void incrementStockQuantity(int pNo, String size) throws Exception;

    // 정산 완료에서 다른 상태로 변경 시 상품 옵션의 재고 수량 감소
    void decrementStockQuantity(int pNo, String size) throws Exception;
}
