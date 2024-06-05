package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.dto.Page;

@Mapper
public interface BrandMapper {

    // 브랜드 목록
    public List<Brand> list() throws Exception;

    // 브랜드 목록 - 페이징, 검색
    public List<Brand> brandList(@Param("page") Page page, @Param("keyword") String keyword) throws Exception;

     // 전체 데이터 개수 가져오기
     public int getTotalCount(@Param("keyword") String keyword) throws Exception;

    // 브랜드 등록
    public int insert(Brand brand) throws Exception;

    // 마지막 브랜드 번호 가져오기 (첨부파일 등록시 사용됨)
    public int maxNo() throws Exception;
    
}
