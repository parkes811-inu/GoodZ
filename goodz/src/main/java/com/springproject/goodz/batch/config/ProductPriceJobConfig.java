package com.springproject.goodz.batch.config;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.mapper.ProductMapper;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class ProductPriceJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ProductMapper productMapper;

    public ProductPriceJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ProductMapper productMapper) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.productMapper = productMapper;
    }

    @Bean
    public Job updateProductPricesJob() {
        return jobBuilderFactory.get("updateProductPricesJob")
                .start(updateProductPricesStep())
                .build();
    }

    @Bean
    public Step updateProductPricesStep() {
        return stepBuilderFactory.get("updateProductPricesStep")
                .<Product, Product>chunk(10)
                .reader(productItemReader())
                .processor(productItemProcessor())
                .writer(productItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Product> productItemReader() {
        return () -> {
            List<Product> productList = productMapper.list();
            return (Product) productList.iterator();
        };
    }

    @Bean
    public ItemProcessor<Product, Product> productItemProcessor() {
        return product -> {
            // 조회수, 관심 수, 구매 건수에 따른 가격 변동 로직 구현
            if (product.getViews() > 100) {
                product.setInitialPrice(product.getInitialPrice() + 1000);
            } else {
                product.setInitialPrice(product.getInitialPrice() - 1000);
            }
            return product;
        };
    }

    @Bean
    public ItemWriter<Product> productItemWriter() {
        return products -> {
            for (Product product : products) {
                productMapper.updateProduct(product);
            }
        };
    }
}
