package com.springproject.goodz.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductImage;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.mapper.ProductMapper;
import com.springproject.goodz.product.mapper.ProductOptionMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductOptionMapper productOptionMapper;
    
    @Override
    public List<Product> list() throws Exception {
        return productMapper.list();
    }

    @Override
    public List<Product> newArrivals() throws Exception {
        return productMapper.newArrivals();
    }

    @Override
    public int insert(Product product) throws Exception {
        log.info("상품 등록 처리 진행중...");

        int result = productMapper.insert(product);

        if (result > 0) {
            log.info("상품 등록 처리 완료");

            // 상품이 등록된 후 옵션 추가
            List<ProductOption> options = product.getOptions();
            if (options != null && !options.isEmpty()) {
                for (ProductOption option : options) {
                    option.setPNo(product.getPNo());
                    productOptionMapper.insertProductOption(option);
                }
            }
        }

        return result;
    }

    @Override
    public List<Product> top() throws Exception {
        return productMapper.top();
    }

    @Override
    public List<Product> pants() throws Exception {
        return productMapper.pants();
    }

    @Override
    public List<Product> shoes() throws Exception {
        return productMapper.shoes();
    }

    @Override
    public List<Product> accessory() throws Exception {
        return productMapper.accessory();
    }

    @Override
    public Product getProductBypNo(int pNo) throws Exception {
        return productMapper.getProductBypNo(pNo);
    }

    @Override
    public List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception {
        return productOptionMapper.getProductOptionsByProductId(pNo);
    }

    @Override
    public int insertProductOption(ProductOption productOption) throws Exception {
        return productOptionMapper.insertProductOption(productOption);
    }

    public List<ProductImage> getProductImagesByProductId(int pNo) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductImagesByProductId'");
    }
}
