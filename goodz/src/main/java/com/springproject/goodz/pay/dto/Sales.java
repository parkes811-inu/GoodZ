package com.springproject.goodz.pay.dto;

import java.util.Date;
import java.util.List;

import com.springproject.goodz.product.dto.ProductOption;

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

    // 추가: 상품 이름, 상품 이미지 URL, 옵션 정보 등 필요한 정보 추가
    private String productName; 
    private String imageUrl;
    private List<ProductOption> options; // 연관된 옵션 목록
    private String formattedSalePrice; // 구매 가격을 원화 형식으로 표현하기 위한 변수
    private String bName;
}

