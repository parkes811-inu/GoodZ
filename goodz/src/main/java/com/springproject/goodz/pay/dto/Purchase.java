package com.springproject.goodz.pay.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Purchase {
    private int purchaseNo;         // 구매 번호
    private String userId;          // 유저 아이디
    private int pNo;                // 상품 번호
    private int purchasePrice;      // 구매 가격
    private String paymentMethod;   // 결제 방법
    private String purchaseState;   // 결제 상태 ENUM('pending', 'shipped', 'delivered', 'cancelled')
    private Date purchaseDate;      // 결제 날짜
    private int optionId;        // 상품 옵션 아이디
}
