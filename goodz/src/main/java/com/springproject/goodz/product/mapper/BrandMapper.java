package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.product.dto.Brand;

@Mapper
public interface BrandMapper {

    // 브랜드 목록
    public List<Brand> list() throws Exception;

    // 브랜드 등록
    public int insert(Brand brand) throws Exception;

    // 마지막 브랜드 번호 가져오기 (첨부파일 등록시 사용됨)
    public int maxNo() throws Exception;
    
}
