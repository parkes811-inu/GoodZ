package com.springproject.goodz.product.service;

import java.util.List;

import com.springproject.goodz.product.dto.Product;

public interface ProductService {
    
    // 상품 목록 (페이징 추후 추가) - 관리자 전용 다 볼 수 있음
    public List<Product> list() throws Exception;

    // 상품 등록
    public int insert(Product product) throws Exception;

    // 상품 목록 - 상의
    public List<Product> top() throws Exception;
}
