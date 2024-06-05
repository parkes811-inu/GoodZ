package com.springproject.goodz.product.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

<<<<<<< HEAD
import com.mysql.cj.protocol.ExportControlled;
=======
import com.springproject.goodz.product.dto.Page;
>>>>>>> 84a91898e9f66f583851024518a317d9cfc9df0c
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.dto.ProductImage;

public interface ProductService {
    
    // 상품 목록
    List<Product> list() throws Exception;

    // 상품 목록 - 관리자 페이징 + 검색
    List<Product> productList(Page page, String keyword) throws Exception;
    
    // 전체 데이터 개수 가져오기
    int getTotalCount(String keyword) throws Exception;

    // 상품 목록 - 메인화면에 최근입고 4개
    List<Product> newArrivals() throws Exception;

    // 상품 등록
    int insert(Product product, int mainImgIndex) throws Exception;

    // 상품 목록 - 상의
    List<Product> top() throws Exception;

    // 상품 목록 - 하의
    List<Product> pants() throws Exception;

    // 상품 목록 - 신발
    List<Product> shoes() throws Exception;

    // 상품 목록 - 악세사리
    List<Product> accessory() throws Exception;

    // 상품 상세 조회
    Product getProductBypNo(int pNo) throws Exception;

    // 상품 옵션 등록
    int insertProductOption(ProductOption productOption) throws Exception;
    
    // 상품의 옵션 목록 조회
    List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception;

    // 상품의 이미지 목록 조회
    List<ProductImage> getProductImagesByProductId(int pNo) throws Exception;
    
    // 상품 발매가 저장 -> priceHistory
    void makeHistory(int pNo, String size, int initialPrice) throws Exception;

    // 제품과 최신 가격 변동 정보 조회
    public List<Product> UsedInPay(int pNo) throws Exception;

    // 같은 브랜드 상품 조회
    // 상세 페이지 내에서 같은 브랜드 상품 조회
    public List<Product> findSameBrandProducts(@Param("brand") String brand, 
                                               @Param("category") String category, 
                                               @Param("pNo") int pNo,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit) throws Exception;
}
