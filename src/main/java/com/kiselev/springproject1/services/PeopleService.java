package com.kiselev.springproject1.services;

import com.kiselev.springproject1.models.Book;
import com.kiselev.springproject1.models.Person;
import com.kiselev.springproject1.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){return peopleRepository.findAll();}

    public Person findAny(int id){
        Optional<Person> foundedPerson = peopleRepository.findById(id);
        return foundedPerson.orElse(null);
    }

    @Transactional
    public void savePerson(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void deletePerson(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findPersonByName(String name){
        return(peopleRepository.findByName(name));
    }

    public List<Book> findBooksByPersonId(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if(person.isPresent()){
            Hibernate.initialize(person.get().getBooks());//safe load books for iterate
            person.get().getBooks().forEach(book -> {
                long timePeriod = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                        if(timePeriod > 84_000_000){ //expired period more than 10 days
                            book.setExpired(true);
                        }
            });
            return person.get().getBooks();
        } else return Collections.emptyList();

    }
}
