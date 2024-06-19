package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.product.dto.Pricehistory;

@Mapper
public interface PriceHistoryMapper {
    
    // 상품 등록 시 발매 가격 및 초기 가격 저장을 위한 메소드
    public void makeHistory(@Param("pNo") int pNo, @Param("size") String size, @Param("initialPrice") int initialPrice);

    // 기간 별 상품 가격 정보 조회
    List<Pricehistory> findPriceHistoryLastWeek(@Param("pNo") int pNo, @Param("size") String size);

    List<Pricehistory> findPriceHistoryLastMonth(@Param("pNo") int pNo, @Param("size") String size);

    List<Pricehistory> findPriceHistoryLast3Months(@Param("pNo") int pNo, @Param("size") String size);

    List<Pricehistory> findPriceHistoryAllTime(@Param("pNo") int pNo, @Param("size") String size);

}
