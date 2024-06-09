package com.springproject.goodz.admin.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.product.dto.Page;

public interface AdminService {

    // 유저가 판매한 상품 전체 조회
    List<Map<String, Object>> userSaleList(Page page) throws Exception;
    
    // 유저가 판매한 상품 전체 개수
    public int getTotalCount() throws Exception;

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

    // 정산 완료시 product_option에 없는 사이즈면 추가
    public void handleProductOption(int pNo, String size, int optionPrice) throws Exception;

    // 상품 발매가 가져오기
    public int getInitialPrice(@Param("pNo") int pNo) throws Exception;


     // ---------------------------------------------------------------------------------------------------------------------------------------




    // 유저가 구매한 상품 전체 조회
    List<Map<String, Object>> userPurchaseList(Page page) throws Exception;

    // 유저가 구매한 상품 전체 개수
    public int TotalCount() throws Exception;

    // 상태별 판매 현황 카운트
    List<Map<String, Object>> countUserPurchaseByState() throws Exception;

    // 유저가 구매한 상품 단일조회
    public Purchase userPurchase(int purchaseNo) throws Exception;

    // 유저가 구매한 상품 상태 변경 (상태만 변경)
    void updatePurchaseState(int purchaseNo, String purchaseState, String currentState, int pNo, String size) throws Exception;

    // 운송장 번호와 택배사 정보 업데이트 (상태 변경 없이)
    void updateTrackingInfo(int purchaseNo, String trackingNo) throws Exception;

    // 상태가 배송중으로 바뀌면 수량 -1
    public void decreaseProductStock(int pNo, String size) throws Exception;

    // 상태가 배송중에서 다른 상태가 되면 수량 +1
    public void increaseProductStock(int pNo, String size) throws Exception;
}
