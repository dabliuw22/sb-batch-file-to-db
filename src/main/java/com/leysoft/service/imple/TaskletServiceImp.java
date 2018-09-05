
package com.leysoft.service.imple;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.leysoft.service.inter.BatchService;

@Service(
        value = "taskletServiceImp")
public class TaskletServiceImp implements BatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskletServiceImp.class);

    @Autowired
    @Qualifier(
            value = "asyncJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(
            value = "jobTasklet")
    private Job job;

    @Override
    public void run() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("jobParameters", new Date()).toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            LOGGER.error("error -> {}", e);
        }
    }
}
