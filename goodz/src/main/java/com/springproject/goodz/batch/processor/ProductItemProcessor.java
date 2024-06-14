package com.springproject.goodz.batch.processor;

import com.springproject.goodz.batch.mapper.BatchProductMapper;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    @Autowired
    private BatchProductMapper batchProductMapper;

    @Override
    public Product process(Product product) throws Exception {
        List<ProductOption> options = batchProductMapper.findProductOptionsByProductId(product.getPNo());
        Date oneWeekAgo = new Date(System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000));

        for (ProductOption option : options) {
            int purchaseCount = batchProductMapper.countPurchasesByProductIdSince(product.getPNo(), option.getOptionId() ,oneWeekAgo);
            int wishListCount = batchProductMapper.countWishListByProductId(product.getPNo());
            int salesCount = batchProductMapper.countSalesByProductIdSince(product.getPNo(), product.getSize(), oneWeekAgo);
            int viewsCount = (int) (product.getViews() * 0.1);

            int score = viewsCount + purchaseCount + wishListCount - salesCount;
            int newPrice = option.getOptionPrice() + (score * 1000);

            if (newPrice < product.getInitialPrice()) {
                newPrice = product.getInitialPrice();
            }

            // PriceHistory에 이전 가격 저장
            batchProductMapper.insertPriceHistory(product.getPNo(), option.getSize(), option.getOptionPrice());

            // 새 가격으로 업데이트
            option.setOptionPrice(newPrice);
            batchProductMapper.updateProductOption(option);
        }

        return product;
    }
}
