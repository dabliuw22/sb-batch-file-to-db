package com.leysoft.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;

import com.leysoft.mapper.PersonFieldSetMapper;
import com.leysoft.model.Person;

@Configuration
public class ChunkConfiguration {
	
	@Autowired
	@Qualifier(value = "chunkPersonItemWriter")
	private ItemWriter<Person> personItemWriter;
	
	@Bean(name = {"asyncJobLauncher"})
	public JobLauncher asyncJobLauncher(JobRepository jobRepository, 
			TaskExecutor asyncTaskExecutor) {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(asyncTaskExecutor);
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}
	
	@Bean(name = {"personItemReader"})
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
	
	@Bean(name = {"stepChunk"})
	public Step stepChunk(StepBuilderFactory stepBuilderFactory, 
			FlatFileItemReader<Person> personItemReader) {
		return stepBuilderFactory.get("stepChunk").<Person, Person>chunk(2)
				.reader(personItemReader)
				.writer(personItemWriter).build();
	}
	
	@Bean(name = {"jobChunk"})
	public Job jobChunk(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("jobChunk")
				.start(step).build();
	}
}
