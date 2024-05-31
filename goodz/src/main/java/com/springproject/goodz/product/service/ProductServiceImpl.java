package com.springproject.goodz.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.dto.ProductImage;
import com.springproject.goodz.product.mapper.ProductMapper;
import com.springproject.goodz.product.mapper.ProductOptionMapper;
import com.springproject.goodz.product.mapper.ProductImageMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductOptionMapper productOptionMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    /**
     * 상품 목록 조회
     */
    @Override
    public List<Product> list() throws Exception {
        return productMapper.list();
    }

    /**
     * 메인화면에 최근 상품 4개 띄우기
     */
    @Override
    public List<Product> newArrivals() throws Exception {
        return productMapper.newArrivals();
    }

    /**
     * 상품 등록 처리
     */
    @Override
    public int insert(Product product) throws Exception {
        log.info("상품 등록 처리 진행중...");

        int result = productMapper.insert(product);

        if (result > 0) {
            log.info("상품 등록 처리 완료");

            // 상품이 등록된 후 옵션 및 이미지를 추가
            List<ProductOption> options = product.getOptions();
            if (options != null && !options.isEmpty()) {
                for (ProductOption option : options) {
                    option.setPNo(product.getPNo());
                    productOptionMapper.insertProductOption(option);
                }
            }

            List<ProductImage> images = product.getImages();
            if (images != null && !images.isEmpty()) {
                for (ProductImage image : images) {
                    image.setPNo(product.getPNo());
                    productImageMapper.insertProductImage(image);
                }
            }
        }

        return result;
    }

    /**
     * 상의만 보기
     */
    @Override
    public List<Product> top() throws Exception {
        return productMapper.top();
    }

    /**
     * 하의만 보기
     */
    @Override
    public List<Product> pants() throws Exception {
        return productMapper.pants();
    }

    /**
     * 신발만 보기
     */
    @Override
    public List<Product> shoes() throws Exception {
        return productMapper.shoes();
    }

    /**
     * 악세사리만 보기
     */
    @Override
    public List<Product> accessory() throws Exception {
        return productMapper.accessory();
    }

    /**
     * 상품 상세 조회
     */
    @Override
    public Product getProductBypNo(int pNo) throws Exception {
        return productMapper.getProductBypNo(pNo);
    }

    /**
     * 상품의 옵션 목록 조회
     */
    @Override
    public List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception {
        return productOptionMapper.getProductOptionsByProductId(pNo);
    }

    /**
     * 상품의 이미지 목록 조회
     */
    @Override
    public List<ProductImage> getProductImagesByProductId(int pNo) throws Exception {
        return productImageMapper.getProductImagesByProductId(pNo);
    }
}
