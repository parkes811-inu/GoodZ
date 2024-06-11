package com.springproject.goodz.batch.reader;

import com.springproject.goodz.batch.mapper.BatchProductMapper;
import com.springproject.goodz.product.dto.Product;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class ProductItemReader implements ItemReader<Product> {

    @Autowired
    private BatchProductMapper batchProductMapper;

    private Iterator<Product> productIterator;

    @Override
    public Product read() throws Exception {
        // productIterator가 null일 때, 모든 제품을 조회하여 Iterator로 변환
        if (productIterator == null) {
            List<Product> products = batchProductMapper.findAllProducts();
            productIterator = products.iterator();
        }
        // Iterator에서 다음 제품을 반환
        if (productIterator.hasNext()) {
            return productIterator.next();
        } else {
            // 더 이상 제품이 없으면 null 반환
            return null;
        }
    }
}
