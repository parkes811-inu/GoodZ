package com.springproject.goodz.batch.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;

@Mapper
public interface BatchProductMapper {
    
    // 등록된 모든 제품 조회
    List<Product> findAllProducts();

    // 특정 제품의 옵션 조회
    List<ProductOption> findProductOptionsByProductId(int pNo);

    // 제품의 조회수 초기화
    void updateProductViews(int pNo);

    // 특정 제품의 특정 날짜 이후의 구매 횟수 조회
    int countPurchasesByProductIdSince(@Param("pNo") int pNo, @Param("optionId") int optionId, @Param("since") Date since);

    // 특정 제품의 관심 목록 등록 횟수 조회
    int countWishListByProductId(int pNo);

    // 특정 제품의 특정 날짜 이후의 판매 횟수를 조회
    int countSalesByProductIdSince(@Param("pNo") int pNo, @Param("size") String size ,@Param("since") Date since);

    // 가격 변동 내역 삽입
    void insertPriceHistory(@Param("pNo") int pNo, @Param("size") String size, @Param("previousPrice") int previousPrice);

    // 제품 옵션 가격 업데이트
    void updateProductOption(ProductOption productOption);
}
