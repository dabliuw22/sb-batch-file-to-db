
package com.leysoft.batch.filter;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.leysoft.model.Person;

@StepScope
@Component(
        value = "personFilterItemProcessor")
public class PersonFilterItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person item) throws Exception {
        if (item.getAge() % 2 == 0) {
            return item;
        }
        return null;
    }
}
