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

    // optionId 로 옵션 단일 조회
    public ProductOption getProductOptionByOptionId(int optionId) throws Exception;

    // 구매 업데이트 성공 시 재고 수량 - 1
    public void minusQuantityByProductId(int optionId) throws Exception;

    // 옵션 아이디 별 재고 수량 확인
    public int checkStockQuantity(int optionId) throws Exception;

    // 상품 상태 업데이트 
    public void changeStatus(int optionId) throws Exception;
}
