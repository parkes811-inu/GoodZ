package com.springproject.goodz.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.product.dto.Page;

@Mapper
public interface AdminMapper {
    
    // 유저가 판매한 상품 전체 조회
    List<Map<String, Object>> userSaleList(Page page) throws Exception;

    // 유저가 판매한 상품 전체 개수
    public int getTotalCount() throws Exception;

    // 상태별 판매 현황 카운트
    List<Map<String, Object>> countUserSalesByState() throws Exception;

    // 유저가 판매한 상품 단일 조회
    Map<String, Object> userSale(@Param("saleNo") int saleNo) throws Exception;

    // 유저가 판매한 상품 상태 변경
    void updateUserSaleState(@Param("sNo") int sNo, @Param("saleState") String saleState) throws Exception;

    // 판매 완료 시 상품 옵션의 재고 수량 증가 (상태가 completed가 되었을때)
    void incrementStockQuantity(@Param("pNo") int pNo, @Param("size") String size) throws Exception;

    // 정산 완료에서 다른 상태로 변경 시 상품 옵션의 재고 수량 감소
    void decrementStockQuantity(@Param("pNo") int pNo, @Param("size") String size) throws Exception;

    // 정산 완료시 사이즈가 있는지 확인
    boolean checkProductOptionExists(@Param("pNo") int pNo, @Param("size") String size) throws Exception;

    // 정산 완료시 사이즈가 없으면 추가
    void insertProductOption(@Param("pNo") int pNo, @Param("size") String size, @Param("optionPrice") int optionPrice) throws Exception;

    // initial_price 가져오기
    int getInitialPrice(@Param("pNo") int pNo) throws Exception;


    // ---------------------------------------------------------------------------------------------------------------------------------------



    // 유저가 구매한 상품 전체 조회
    List<Map<String, Object>> userPurchaseList(Page page) throws Exception;

    // 유저가 구매한 상품 전체 개수
    int TotalCount() throws Exception;

    // 상태별 판매 현황 카운트
    List<Map<String, Object>> countUserPurchaseByState() throws Exception;

    // 유저가 구매한 상품 단일조회
    Purchase userPurchase(@Param("purchaseNo") int purchaseNo) throws Exception;

    // 유저가 구매한 상품 상태 변경 (상태만 변경)
    int updatePurchaseState(@Param("purchaseNo") int purchaseNo, @Param("purchaseState") String purchaseState) throws Exception;
    
    // 운송장 번호와 택배사 정보 업데이트 (상태 변경 없이)
    int updateTrackingInfo(@Param("purchaseNo") int purchaseNo, @Param("trackingNo") String trackingNo) throws Exception;

    // 재고 수량 감소 메서드
    int decreaseProductStock(@Param("pNo") int pNo, @Param("size") String size) throws Exception;

    // 재고 수량 증가 메서드
    int increaseProductStock(@Param("pNo") int pNo, @Param("size") String size) throws Exception;

}
