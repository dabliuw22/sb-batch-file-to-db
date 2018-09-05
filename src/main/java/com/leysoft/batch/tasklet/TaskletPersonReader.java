
package com.leysoft.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;
import com.leysoft.service.inter.FileService;

@Component(
        value = "taskletPersonReader")
public class TaskletPersonReader implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskletPersonReader.class);

    private List<Person> persons;

    @Autowired
    private FileService fileService;

    @PostConstruct
    private void init() {
        this.persons = new ArrayList<>();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        Person person = fileService.read();
        while (person != null) {
            persons.add(person);
            LOGGER.info("reader -> {}", person);
            person = fileService.read();
        }
        return RepeatStatus.FINISHED;
    }
}
