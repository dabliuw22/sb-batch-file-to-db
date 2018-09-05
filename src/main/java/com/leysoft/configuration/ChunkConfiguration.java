
package com.leysoft.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;

import com.leysoft.batch.chunk.ChunkPersonItemWriter;
import com.leysoft.batch.validator.PersonValidator;
import com.leysoft.mapper.PersonFieldSetMapper;
import com.leysoft.model.Person;

@Configuration
public class ChunkConfiguration {

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
                "personValidatingItemProcessor"
            })
    public ValidatingItemProcessor<Person> personValidatingItemProcessor() {
        ValidatingItemProcessor<Person> itemProcessor =
                new ValidatingItemProcessor<>(new PersonValidator());
        itemProcessor.setFilter(true);
        return itemProcessor;
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
                "stepChunkOne"
            })
    public Step stepChunkOne(StepBuilderFactory stepBuilderFactory,
            FlatFileItemReader<Person> personItemReader,
            @Qualifier("personItemProcessor") ItemProcessor<Person, Person> personItemProcessor,
            ItemWriter<Person> personItemWriter, TaskExecutor asyncTaskExecutor) {
        return stepBuilderFactory.get("stepChunkOne").<Person, Person> chunk(5)
                .reader(personItemReader).processor(personItemProcessor).writer(personItemWriter)
                .taskExecutor(asyncTaskExecutor).build();
    }

    @Bean(
            name = {
                "flowChunk"
            })
    public Flow flowChunk(Step stepChunkOne) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowChunk");
        flowBuilder.start(stepChunkOne).end();
        return flowBuilder.build();
    }

    @Bean(
            name = {
                "jobChunk"
            })
    public Job jobChunk(JobBuilderFactory jobBuilderFactory, Step stepChunk) {
        return jobBuilderFactory.get("jobChunk").start(stepChunk).build();
    }
}
