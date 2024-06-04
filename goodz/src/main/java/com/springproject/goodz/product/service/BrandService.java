package com.springproject.goodz.product.service;

import java.util.List;

import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.dto.Page;

public interface BrandService {

    // 브랜드 목록
    public List<Brand> list() throws Exception;

    // 브랜드 목록 - 페이징 + 검색
    public List<Brand> brandList(Page page, String keyword) throws Exception;

    // 전체 데이터 개수 가져오기
    public int getTotalCount(String keyword) throws Exception;

    // 브랜드 등록
    public int insert(Brand brand) throws Exception;

    // 마지막 브랜드 번호 가져오기 (첨부파일 등록시 사용됨)
    // public int maxNo() throws Exception;

}

