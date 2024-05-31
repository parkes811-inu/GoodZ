package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.springproject.goodz.product.dto.ProductOption;

@Mapper
public interface ProductOptionMapper {

    // 특정 상품의 옵션 목록 조회
    // @Select("SELECT * FROM product_option WHERE p_no = #{pNo}")
    List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception;

    // 새로운 상품 옵션 추가
    // @Insert("INSERT INTO product_option (p_no, size, option_price, stock_quantity, sku, status) " +
    //         "VALUES (#{pNo}, #{size}, #{optionPrice}, #{stockQuantity}, #{sku}, #{status})")
    int insertProductOption(ProductOption productOption) throws Exception;
}
