package com.kiselev.springproject1.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 *
 */
@Entity
@Table(name="Person")
public class Person {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "'Name' must not be empty")
    @Size(min = 3, max = 50, message = "number of characters must be from 3 to 50")
    @Column(name="name")
    private String name;

    @Min(value = 1930, message = "Year of birth must be over 1930")
    @Column(name="age")
    private int age;
    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
