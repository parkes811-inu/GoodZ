package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.product.dto.ProductOption;

@Mapper
public interface ProductOptionMapper {

    // 특정 상품의 옵션 목록 조회
    List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception;

    // admin 페이지에서 특정 상품의 옵션 목록 조회 - 판매 중지인 옵션까지 포함
    List<ProductOption> adminOptionsByProductId(int pNo) throws Exception;

    // 새로운 상품 옵션 추가
    int insertProductOption(ProductOption productOption) throws Exception;
}
