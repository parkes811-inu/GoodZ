package com.springproject.goodz.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job updateProductPricesJob;

    @Autowired
    public BatchScheduler(JobLauncher jobLauncher, Job updateProductPricesJob) {
        this.jobLauncher = jobLauncher;
        this.updateProductPricesJob = updateProductPricesJob;
    }

    // @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Scheduled(initialDelay = 60000) // 애플리케이션 시작 후 1분 후에 한 번만 실행
    public void runBatchJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        jobLauncher.run(updateProductPricesJob, new JobParameters());
    }
}
