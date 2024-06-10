package com.springproject.goodz.batch.writer;

import com.springproject.goodz.product.dto.Product;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductItemWriter implements ItemWriter<Product> {
    @Override
    public void write(List<? extends Product> products) {
        // 여기서 DB에 저장하는 로직을 작성하세요.
        for (Product product : products) {
            // 예: productRepository.save(product);
        }
    }
}
