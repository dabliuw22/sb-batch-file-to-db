
package com.leysoft.batch.tasklet;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;
import com.leysoft.util.Util;

@Component(
        value = "taskletPersonWriter")
public class TaskletPersonWriter implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskletPersonWriter.class);

    private List<Person> persons;

    @Autowired
    private NamedParameterJdbcOperations jdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
            throws Exception {
        persons.forEach(person -> {
            LOGGER.info("writer: person -> {}", person);
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("name", person.getName()).addValue("birthday", person.getBirthday())
                    .addValue("age", person.getAge());
            jdbcTemplate.update(Util.SqlConstant.INSERT_PERSON, parameters);
        });
        return RepeatStatus.FINISHED;
    }
}
