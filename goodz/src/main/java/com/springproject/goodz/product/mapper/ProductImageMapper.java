package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.springproject.goodz.product.dto.ProductImage;

@Mapper
public interface ProductImageMapper {

    // 특정 상품의 이미지 목록 조회
    // @Select("SELECT * FROM product_image WHERE p_no = #{pNo}")
    public List<ProductImage> getProductImagesByProductId(int pNo) throws Exception;

    // 새로운 상품 이미지 추가
    // @Insert("INSERT INTO product_image (p_no, image_url) VALUES (#{pNo}, #{imageUrl})")
    public int insertProductImage(ProductImage productImage) throws Exception;
}
