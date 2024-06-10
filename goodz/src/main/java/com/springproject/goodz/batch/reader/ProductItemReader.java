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
        if (productIterator == null) {
            List<Product> products = batchProductMapper.findAllProducts();
            productIterator = products.iterator();
        }
        if (productIterator.hasNext()) {
            return productIterator.next();
        } else {
            return null;
        }
    }
}
