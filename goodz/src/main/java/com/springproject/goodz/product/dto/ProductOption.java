package com.springproject.goodz.product.dto;

import lombok.Data;

@Data
public class ProductOption {
    private int optionId;             
    private int pNo;            // 상품 번호
    private String size;        // 옵션 크기
    private int optionPrice;    // 옵션 가격
    private int stockQuantity;  // 재고 수량
    private String status;      // 상태 ('on' 또는 'off')
    private int addedStockQuantity; // 추가 수량

}
