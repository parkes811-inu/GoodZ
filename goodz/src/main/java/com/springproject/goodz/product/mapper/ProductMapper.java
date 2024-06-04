package com.springproject.goodz.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.product.dto.Page;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.dto.ProductImage;

@Mapper
public interface ProductMapper {
    
    // 상품 목록 - 메인화면에 최근입고 4개
    public List<Product> newArrivals() throws Exception;

    // 상품 목록 - 마이페이지 메인에 4개, (알아서 짜셈)
    public List<Product> latestAdd() throws Exception;

    // 상품 목록 
    public List<Product> list() throws Exception;

    // 상품 목록 - 관리자 페이징 + 검색
    List<Product> productList(@Param("page") Page page, @Param("keyword") String keyword) throws Exception;
    
    // 전체 데이터 개수 가져오기
    public int getTotalCount(@Param("keyword") String keyword) throws Exception;

    // <관심>상품 목록 (페이징 추후 추가) - 볼 수 있음(알아서 짜셈)
    public List<Product> userlist() throws Exception;
    
    // 상품 목록 - 상의
    public List<Product> top() throws Exception;

    // 상품 목록 - 하의
    public List<Product> pants() throws Exception;

    // 상품 목록 - 신발
    public List<Product> shoes() throws Exception;

    // 상품 목록 - 악세서리
    public List<Product> accessory() throws Exception;

    // 상품 조회
    public Product select(int pNo) throws Exception;

    // 상품 등록
    public int insert(Product product) throws Exception;

    // 상품 수정
    public int update(Product product) throws Exception;

    // 조회수 증가
    public int views(int no) throws Exception;

    // 상품 상세 조회
    public Product getProductBypNo(int pNo) throws Exception;

    // 상품의 옵션 목록 조회
    public List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception;

    // 상품의 이미지 목록 조회
    public List<ProductImage> getProductImagesByProductId(int pNo) throws Exception;

    // 상품 옵션 추가
    public int insertProductOption(ProductOption productOption) throws Exception;

    // 상품 이미지 추가
    public int insertProductImage(ProductImage productImage) throws Exception;

    // 제품과 최신 가격 변동 정보 조회
    public List<Product> UsedInPay(int pNo) throws Exception;
}
