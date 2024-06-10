package com.springproject.goodz.batch.tasklet;

import com.springproject.goodz.batch.mapper.BatchProductMapper;
import com.springproject.goodz.product.mapper.ProductMapper;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductPricesTasklet implements Tasklet {

    @Autowired
    private BatchProductMapper batchProductMapper;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 상품 가격을 업데이트하는 로직을 추가합니다.
        batchProductMapper.updateProductPrices();
        return RepeatStatus.FINISHED;
    }
}
