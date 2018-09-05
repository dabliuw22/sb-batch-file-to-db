
package com.leysoft.batch.chunk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;

@Component(
        value = "chunkPersonItemWriter")
public class ChunkPersonItemWriter implements ItemWriter<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkPersonItemWriter.class);

    public static final String INSERT_PERSON =
            "insert into persons(name, birthday) values (:name, :birthday)";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void write(List<? extends Person> items) throws Exception {
        items.forEach(person -> {
            LOGGER.info("person -> {}", person);
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("name", person.getName()).addValue("birthday", person.getBirthday());
            jdbcTemplate.update(INSERT_PERSON, parameters);
        });
    }
}
