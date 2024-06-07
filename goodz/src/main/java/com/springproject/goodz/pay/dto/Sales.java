package com.springproject.goodz.pay.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Sales {
    private int sNo;                    // 판매 번호
    private String userId;              // 유저 아이디
    private int pNo;                    // 상품 번호
    private String saleTrackingNo;      // 운송장 번호
    private int salePrice;              // 판매 가격
    private String size;                // 상품 사이즈
    private String address;             // 반송 주소            
    private String saleState;           // 판매 상태 ENUM('pending','reception','checking', 'completed', 'cancelled')
    private Date saleDate;              // 판매 날짜

}

