package com.kiselev.springproject1.util;

import com.kiselev.springproject1.dao.PersonDAO;
import com.kiselev.springproject1.models.Person;
import com.kiselev.springproject1.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 */
@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        // check if Person name exists
        if (peopleService.findPersonByName(person.getName()).isPresent())
            errors.rejectValue("name", "", "This 'Name' is already exist");

        // Check if Person name starts from Capital letter
        if (!Character.isUpperCase(person.getName().codePointAt(0)))
            errors.rejectValue("name", "", "'Name' must starts with Capital letters'");
    }
}
