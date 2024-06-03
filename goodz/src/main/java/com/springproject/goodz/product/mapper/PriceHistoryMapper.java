package com.springproject.goodz.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface PriceHistoryMapper {
    
    // 상품 등록 시 발매 가격 및 초기 가격 저장을 위한 메소드
    public void makeHistory(@Param("pNo") int pNo, @Param("size") String size, @Param("initialPrice") int initialPrice);

}
