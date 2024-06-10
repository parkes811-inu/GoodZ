package com.springproject.goodz.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springproject.goodz.batch.processor.ProductItemProcessor;
import com.springproject.goodz.batch.reader.ProductItemReader;
import com.springproject.goodz.batch.writer.ProductItemWriter;
import com.springproject.goodz.product.dto.Product;

@Configuration
@EnableBatchProcessing
public class ProductPriceJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ProductItemReader productItemReader;

    @Autowired
    private ProductItemProcessor productItemProcessor;

    @Autowired
    private ProductItemWriter productItemWriter;

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
                .reader(productItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .build();
    }
}
