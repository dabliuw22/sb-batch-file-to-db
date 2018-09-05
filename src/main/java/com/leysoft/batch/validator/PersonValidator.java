
package com.leysoft.batch.validator;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import com.leysoft.model.Person;

public class PersonValidator implements Validator<Person> {

    @Override
    public void validate(Person value) throws ValidationException {
        if (value.getName().endsWith("One")) {
            throw new ValidationException("Name no debe terminar en One: " + value);
        }
    }
}
