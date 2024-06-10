package com.springproject.goodz.batch.reader;

import com.springproject.goodz.product.dto.Product;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
public class ProductItemReader implements ItemReader<Product> {
    @Override
    public Product read() throws Exception {
        // 상품 데이터 읽기 로직 구현
        return null;
    }
}
