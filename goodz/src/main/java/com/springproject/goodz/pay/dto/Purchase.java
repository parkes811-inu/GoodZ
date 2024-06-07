package com.springproject.goodz.pay.dto;

import java.util.Date;
import java.util.List;

import com.springproject.goodz.product.dto.ProductOption;
import lombok.Data;

@Data
public class Purchase {
    private int purchaseNo;         // 구매 번호
    private String userId;          // 유저 아이디
    private int pNo;                // 상품 번호
    private int optionId;           // 상품 옵션 아이디
    private String orderId;         // 주문 아이디(토스)
    private int purchasePrice;      // 구매 가격
    private String paymentMethod;   // 결제 방법
    private String address;
    private String purchaseState;   // 결제 상태 ENUM('pending', 'shipped', 'delivered', 'cancelled')
    private Date orderedAt;
    private Date updatedAt;

    // 추가: 상품 이름, 상품 이미지 URL, 옵션 정보 등 필요한 정보 추가
    private String productName; 
    private String imageUrl;
    private List<ProductOption> options; // 연관된 옵션 목록
    private String formattedMinPrice; // 최저가 상품을 원화 형식으로 표현하기 위한 변수
    private String bName;
}
