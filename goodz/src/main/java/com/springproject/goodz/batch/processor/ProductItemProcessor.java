package com.springproject.goodz.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.springproject.goodz.product.dto.Product;

@Component("ProductItem")
public class ProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product product) throws Exception {
        // 상품 가격 업데이트 로직 구현
        return product;
    }
}
