package com.springproject.goodz.product.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Product {
    
    private int pNo;                // 상품 번호
    private String productName;     // 상품 이름
    private int price;              // 상품 가격
    private String bName;           // 브랜드 이름
    private String category;        // 상의, 하의, 신발, 악세사리
    private String size;            // 사이즈
    private int views;              // 조회수
    private int stockQuantity;      // 재고량
    private String imageUrl;        // 이미지 경로
    private Date createdAt;         // 상품 등록 날짜
    private Date updatedAt;         // 상품 수정 날짜

    private List<MultipartFile> productFiles; // 여러 이미지를 위한 필드

    // 첫 번째 이미지 URL을 가져오는 메서드
    public String getFirstImageUrl() {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String firstImageUrl = imageUrl.split(";")[0];
            // System.out.println("First image URL: " + firstImageUrl);
            return firstImageUrl;
        }
        return null;
    }
    
    
}
