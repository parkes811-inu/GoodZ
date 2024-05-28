package com.springproject.goodz.product.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Product {
    
    private int pNo;                // 상품 번호
    private String productName;     // 상품 이름
    private int price;              // 상품 가격
    private String brand;           // 브랜드 이름
    private String category;        // 상의, 하의, 신발, 악세사리
    private String size;            // 사이즈
    private int views;              // 조회수
    private int stockQuantity;      // 재고량
    private String imageUrl;        // 이미지 경로
    private Date createdAt;         // 상품 등록 날짜
    private Date updatedAt;         // 상품 수정 날짜
}
