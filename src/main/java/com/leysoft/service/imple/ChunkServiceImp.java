
package com.leysoft.service.imple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.leysoft.service.inter.ChunkService;

@Service
public class ChunkServiceImp implements ChunkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkServiceImp.class);

    @Autowired
    @Qualifier(
            value = "asyncJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(
            value = "jobChunk")
    private Job job;

    @Override
    public void run() {
        try {
            jobLauncher.run(job, new JobParameters());
        } catch (Exception e) {
            LOGGER.error("error -> {}", e);
        }
    }
}
