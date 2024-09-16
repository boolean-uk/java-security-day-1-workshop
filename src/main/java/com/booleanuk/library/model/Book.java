package com.booleanuk.library.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false )
    @JsonIncludeProperties(value = {"id","first_name", "last_name", "email", "alive"})
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false )
    @JsonIncludeProperties(value = {"id","name", "location"})
    private Publisher publisher;


    @Column(name = "year")
    private int year;

    @Column(name = "genre")
    private String genre;

    public Book(int id) {
        this.id = id;
    }

    public Book(String title, Author author, Publisher publisher, int year, String genre) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genre = genre;
    }
}
