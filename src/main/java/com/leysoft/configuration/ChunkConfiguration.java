
package com.leysoft.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;

import com.leysoft.batch.chunk.ChunkPersonItemWriter;
import com.leysoft.mapper.PersonFieldSetMapper;
import com.leysoft.model.Person;

@Configuration
public class ChunkConfiguration {

    @Bean(
            name = {
                "asyncJobLauncher"
            })
    public JobLauncher asyncJobLauncher(JobRepository jobRepository,
            TaskExecutor asyncTaskExecutor) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(asyncTaskExecutor);
        jobLauncher.setJobRepository(jobRepository);
        return jobLauncher;
    }

    @Bean(
            name = {
                "personItemReader"
            })
    public FlatFileItemReader<Person> personItemReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("data/input/persons.csv"));
        DefaultLineMapper<Person> personLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name", "birthday");
        personLineMapper.setLineTokenizer(tokenizer);
        personLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
        personLineMapper.afterPropertiesSet();
        reader.setLineMapper(personLineMapper);
        return reader;
    }

    @Bean(
            name = {
                "personItemWriter"
            })
    public ItemWriter<Person> personItemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql(ChunkPersonItemWriter.INSERT_PERSON);
        itemWriter.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Bean(
            name = {
                "stepChunk"
            })
    public Step stepChunk(StepBuilderFactory stepBuilderFactory,
            FlatFileItemReader<Person> personItemReader,
            ItemProcessor<Person, Person> personItemProcessor,
            ItemWriter<Person> personItemWriter) {
        return stepBuilderFactory.get("stepChunk").<Person, Person> chunk(2)
                .reader(personItemReader).processor(personItemProcessor).writer(personItemWriter)
                .build();
    }

    @Bean(
            name = {
                "flowChunk"
            })
    public Flow flowChunk(Step stepChunk) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowChunk");
        flowBuilder.start(stepChunk).end();
        return flowBuilder.build();
    }

    @Bean(
            name = {
                "jobChunk"
            })
    public Job jobChunk(JobBuilderFactory jobBuilderFactory, Flow flowChunk) {
        return jobBuilderFactory.get("jobChunk").start(flowChunk).end().build();
    }
}
