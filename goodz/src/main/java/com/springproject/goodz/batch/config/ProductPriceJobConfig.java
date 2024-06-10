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

// 배치 작업 설정 클래스 어노테이션
@Configuration
@EnableBatchProcessing
public class ProductPriceJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ProductItemReader productItemReader;
    private final ProductItemProcessor productItemProcessor;
    private final ProductItemWriter productItemWriter;

    public ProductPriceJobConfig(JobBuilderFactory jobBuilderFactory, 
                                 StepBuilderFactory stepBuilderFactory,
                                 ProductItemReader productItemReader,
                                 ProductItemProcessor productItemProcessor,
                                 ProductItemWriter productItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.productItemReader = productItemReader;
        this.productItemProcessor = productItemProcessor;
        this.productItemWriter = productItemWriter;
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
                .reader(productItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .build();
    }
}
