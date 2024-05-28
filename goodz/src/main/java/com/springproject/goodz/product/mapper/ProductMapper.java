package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.product.dto.Product;

@Mapper
public interface ProductMapper {
    
    // 상품 목록 - 메인화면에 최근입고 4개, (알아서 짜셈)
    public List<Product> newArrivals() throws Exception;

    // 상품 목록 - 마이페이지 메인에 4개, (알아서 짜셈)
    public List<Product> latestAdd() throws Exception;

    // 상품 목록 (페이징 추후 추가) - 관리자 전용 다 볼 수 있음
    public List<Product> list() throws Exception;

    // <관심>상품 목록 (페이징 추후 추가) -  볼 수 있음(알아서 짜셈)하의
    public List<Product> userlist() throws Exception;
    
    // 상품 목록 - 상의
    public List<Product> top() throws Exception;

    // 상품 목록 - 하의
    public List<Product> pants() throws Exception;

    // 상품 목록 - 신발
    public List<Product> shoes() throws Exception;

    // 상품 목록 - 악세서리
    public List<Product> acc() throws Exception;

    // 상품 조회
    public Product select(int pNo) throws Exception;

    // 상품 등록
    public int insert(Product product) throws Exception;

    // 상품 수정
    public int update(Product product) throws Exception;

    // 조회수 증가
    public int views(int no) throws Exception;


}
