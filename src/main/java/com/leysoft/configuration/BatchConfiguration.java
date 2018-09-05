
package com.leysoft.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final String ISOLATION_LEVEL = "ISOLATION_SERIALIZABLE";

    @Bean
    public JobRepository jobRepository(DataSource dataSource,
            PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactory = new JobRepositoryFactoryBean();
        jobRepositoryFactory.setDataSource(dataSource);
        jobRepositoryFactory.setTransactionManager(transactionManager);
        jobRepositoryFactory.setDatabaseType(DatabaseType.H2.name());
        jobRepositoryFactory.setIsolationLevelForCreate(ISOLATION_LEVEL);
        jobRepositoryFactory.afterPropertiesSet();
        return jobRepositoryFactory.getObject();
    }

    @Bean(
            name = {
                "asyncJobLauncher"
            })
    public JobLauncher asyncJobLauncher(JobRepository jobRepository, TaskExecutor asyncTaskExecutor)
            throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(asyncTaskExecutor);
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
