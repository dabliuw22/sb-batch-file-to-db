
package com.leysoft.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskletConfiguration {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier(
            value = "taskletPersonProcessor")
    private Tasklet taskletPersonProcessor;

    @Autowired
    @Qualifier(
            value = "taskletPersonWriter")
    private Tasklet taskletPersonWriter;

    @Bean(
            name = {
                "stepTaskletTwo"
            })
    public Step stepTaskletTwo() {
        return stepBuilderFactory.get("stepTaskletTwo").tasklet(taskletPersonProcessor).build();
    }

    @Bean(
            name = {
                "stepTaskletThree"
            })
    public Step stepTaskletThree() {
        return stepBuilderFactory.get("stepTaskletThree").tasklet(taskletPersonWriter).build();
    }

    @Bean(
            name = {
                "flowTasklet"
            })
    public Flow flowTasklet(Step stepTaskletOne, Step stepTaskletTwo, Step stepTaskletThree) {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowTasklet");
        flowBuilder.start(stepTaskletOne).next(stepTaskletTwo).next(stepTaskletThree).end();
        return flowBuilder.build();
    }

    @Bean(
            name = {
                "jobTasklet"
            })
    public Job jobTasklet(Flow flowTasklet) {
        return jobBuilderFactory.get("jobTasklet").start(flowTasklet).end().build();
    }
}
