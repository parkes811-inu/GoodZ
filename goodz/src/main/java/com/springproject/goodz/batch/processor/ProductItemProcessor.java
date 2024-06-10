package com.springproject.goodz.batch.processor;

import com.springproject.goodz.product.dto.Product;

import lombok.NonNull;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(@NonNull Product product) throws Exception {
        // 조회수, 관심 수, 구매 건수에 따른 가격 변동 로직 구현
        if (product.getViews() == 0) {
            product.setInitialPrice(0);
        } else {
            product.setInitialPrice(product.getInitialPrice() - 1000);
        }
        return product;
    }
}
