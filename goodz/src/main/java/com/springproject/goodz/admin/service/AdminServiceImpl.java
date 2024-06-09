package com.springproject.goodz.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.admin.mapper.AdminMapper;
import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.product.dto.Page;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    // 유저가 판매한 상품 전체 조회
    @Override
    public List<Map<String, Object>> userSaleList(Page page) throws Exception {
        return adminMapper.userSaleList(page);
    }

    // 전체 데이터 개수
    @Override
    public int getTotalCount() throws Exception {
        return adminMapper.getTotalCount();
    }
    
    // 상태별 판매 현황 카운트 
    @Override
    public List<Map<String, Object>> countUserSalesByState() throws Exception {
        return adminMapper.countUserSalesByState();
    }

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

        if (!"completed".equals(currentSaleState) && "completed".equals(saleState)) {
            int pNo = (int) saleDetail.get("productNo");
            int initialPrice = adminMapper.getInitialPrice(pNo);
            handleProductOption(pNo, (String) saleDetail.get("size"), initialPrice);
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

    // 정산 완료시 product_option에 없는 사이즈면 추가
    @Override
    public void handleProductOption(int pNo, String size, int initialPrice) throws Exception {
        boolean exists = adminMapper.checkProductOptionExists(pNo, size);
        int optionPrice = (int) (initialPrice * 1.2); 

        if (!exists) {
            adminMapper.insertProductOption(pNo, size, optionPrice);
        }
    }

    @Override
    public int getInitialPrice(int pNo) throws Exception {
        return adminMapper.getInitialPrice(pNo);
    }


     // ---------------------------------------------------------------------------------------------------------------------------------------


    // 유저가 구매한 전체 내역 조회
    @Override
    public List<Map<String, Object>> userPurchaseList(Page page) throws Exception {
        return adminMapper.userPurchaseList(page);
    }

    // 전체 개수
    @Override
    public int TotalCount() throws Exception {
        return adminMapper.TotalCount();
    }

    // 상태별 구매 현황 카운트 
    @Override
    public List<Map<String, Object>> countUserPurchaseByState() throws Exception {
        return adminMapper.countUserPurchaseByState();
    }

    // 유저가 구매한 상품 단일 조회
    @Override
    public Purchase userPurchase(int purchaseNo) throws Exception {
        return adminMapper.userPurchase(purchaseNo);
    }

    // 유저가 구매한 상품 상태 변경
    @Override
    public void updatePurchaseState(int purchaseNo, String purchaseState, String currentState, int pNo, String size) throws Exception {

        // 상태 변화에 따른 재고 조정 로직
        if (!currentState.equals(purchaseState)) {
            // '배송중'으로 변경되면 재고 수량 감소
            if ("shipping".equals(purchaseState)) {
                if (!"shipping".equals(currentState)) {
                    adminMapper.decreaseProductStock(pNo, size);
                }
            }
            // '배송중'에서 다른 상태로 변경되면 재고 수량 증가 (배송완료 제외)
            else if ("shipping".equals(currentState) && !"shipping".equals(purchaseState) && !"delivered".equals(purchaseState)) {
                adminMapper.increaseProductStock(pNo, size);
            }
            // '배송완료'에서 다른 상태로 변경되면 재고 수량 증가 (배송중 제외)
            else if ("delivered".equals(currentState) && !"shipping".equals(purchaseState) && !"shipping".equals(purchaseState)) {
                adminMapper.increaseProductStock(pNo, size);
            }
        }

        // 구매 상태 업데이트
        adminMapper.updatePurchaseState(purchaseNo, purchaseState);
    }


    // 운송장 번호와 택배사 정보 업데이트 (상태 변경 없이)
    @Override
    public void updateTrackingInfo(int purchaseNo, String trackingNo) throws Exception {

        adminMapper.updateTrackingInfo(purchaseNo, trackingNo);
    }

    // 상태가 배송중으로 바뀌면 재고 -1
    @Override
    public void decreaseProductStock(int pNo, String size) throws Exception {
        adminMapper.decreaseProductStock(pNo, size);
    }

    // 상태가 배송중에서 다른 상태로 바뀌면 재고 +1
    @Override
    public void increaseProductStock(int pNo, String size) throws Exception {
        adminMapper.increaseProductStock(pNo, size);
    }

}




