package com.springproject.goodz.product.dto;


import lombok.Data;

@Data
public class ProductImage {
    private int imgId;              // 이미지 ID
    private int pNo;                // 상품 번호
    private String imageUrl;        // 이미지 경로
}