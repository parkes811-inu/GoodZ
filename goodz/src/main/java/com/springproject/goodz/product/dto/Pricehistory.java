package com.springproject.goodz.product.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Pricehistory {
    private int priceHistoryNo;     // 가격변동 번호
    private int pNo;                // 상품 번호
    private int fluctuatedPrice;    // 변동된 가격
    private Date createdAt;         
    private Date updatedAt;
}
