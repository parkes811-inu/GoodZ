package com.springproject.goodz.product.dto;

import lombok.Data;

@Data
public class ProductOption {
    private int optionId;           // 옵션 ID
    private int pNo;                // 상품 번호
    private String size;            // 사이즈
    private int optionPrice;        // 옵션 가격
    private int stockQuantity;      // 재고량
    private String status;          // 상태 (판매중, 비활성화 등)
}