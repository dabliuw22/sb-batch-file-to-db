
package com.leysoft.batch.chunk;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;

@Primary
@Component(
        value = "chunkPersonItemProcessor")
public class ChunkPersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkPersonItemProcessor.class);

    @Override
    public Person process(Person item) throws Exception {
        LocalDate birthday =
                item.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long age = ChronoUnit.YEARS.between(birthday, LocalDate.now());
        String upperName = item.getName().toUpperCase();
        item.setAge(age);
        item.setName(upperName);
        LOGGER.info("processor: person -> {}", item);
        return item;
    }
}
