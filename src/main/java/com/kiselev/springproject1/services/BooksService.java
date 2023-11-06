package com.kiselev.springproject1.services;

import com.kiselev.springproject1.models.Book;
import com.kiselev.springproject1.models.Person;
import com.kiselev.springproject1.repositories.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear){
        if(sortByYear)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    public List<Book> findAllWithPagination (Integer fromPage, Integer totalPerPage, boolean sorByYear){
        if(sorByYear){
            return booksRepository.findAll(PageRequest.of(fromPage,totalPerPage, Sort.by("year"))).getContent();
        }
        else
            return booksRepository.findAll(PageRequest.of(fromPage,totalPerPage)).getContent();
    }

    public Book findAny(int id){
        Optional<Book> foundAny = booksRepository.findById(id);
        return foundAny.orElse(null);
    }

    public List<Book> findByTitle(String name){
        return booksRepository.findByTitleStartingWith(name);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book newBook){
        Book bookToUpdate = booksRepository.findById(id).get();
        //need save() because add new book (new book is not in Persistence context)
        newBook.setId(id);
        //we need to save link with owner, because we get only Id from the form
        newBook.setOwner(bookToUpdate.getOwner());
        booksRepository.save(newBook);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public Person getBookOwner(int id){
        //no need Hibernate.initialize, we have Eager load Person (One-to-Many)
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void release(int id){
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }

    @Transactional
    public void assign(int id, Person selectedPerson){
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(selectedPerson);
            book.setTakenAt(new Date());
        });

    }
}
