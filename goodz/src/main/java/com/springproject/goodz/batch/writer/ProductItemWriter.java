package com.springproject.goodz.batch.writer;

import com.springproject.goodz.batch.mapper.BatchProductMapper;
import com.springproject.goodz.product.dto.Product;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductItemWriter implements ItemWriter<Product> {

    @Autowired
    private BatchProductMapper batchProductMapper;

    @Override
    public void write(List<? extends Product> products) throws Exception {
        for (Product product : products) {
            batchProductMapper.updateProduct(product);
        }
    }
}
