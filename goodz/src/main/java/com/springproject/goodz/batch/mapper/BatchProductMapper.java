package com.springproject.goodz.batch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.product.dto.Product;

@Mapper
public interface BatchProductMapper {
    
    // 등록된 모든 제품 조회
    List<Product> findAllProducts();

    // 제품 업데이트
    void updateProduct(Product product);
}
