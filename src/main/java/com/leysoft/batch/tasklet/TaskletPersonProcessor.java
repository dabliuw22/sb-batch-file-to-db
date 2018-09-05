
package com.leysoft.batch.tasklet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;

@Component(
        value = "taskletPersonProcessor")
public class TaskletPersonProcessor implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskletPersonWriter.class);

    private List<Person> persons;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        persons.forEach(person -> {
            LocalDate birthday =
                    person.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long age = ChronoUnit.YEARS.between(birthday, LocalDate.now());
            String upperName = person.getName().toUpperCase();
            person.setAge(age);
            person.setName(upperName);
            LOGGER.info("processor: person -> {}", person);
        });
        return RepeatStatus.FINISHED;
    }

}
