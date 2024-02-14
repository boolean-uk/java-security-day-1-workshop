package com.booleanuk.library.controller;

import com.booleanuk.library.model.Author;
import com.booleanuk.library.model.Book;
import com.booleanuk.library.model.Publisher;
import com.booleanuk.library.repository.AuthorRepository;
import com.booleanuk.library.repository.BookRepository;
import com.booleanuk.library.repository.PublisherRepository;
import com.booleanuk.library.response.BookListResponse;
import com.booleanuk.library.response.BookResponse;
import com.booleanuk.library.response.ErrorResponse;
import com.booleanuk.library.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @GetMapping
    public ResponseEntity<BookListResponse> getAllBooks() {
        BookListResponse bookListResponse = new BookListResponse();
        bookListResponse.set(this.bookRepository.findAll());
        return ResponseEntity.ok(bookListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getBookById(@PathVariable int id) {
        Book book = this.bookRepository.findById(id).orElse(null);
        if (book == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No books matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        BookResponse bookResponse = new BookResponse();
        bookResponse.set(book);
        return ResponseEntity.ok(bookResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getGenre() == null ||
                book.getAuthor() == null || book.getPublisher() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create the book, please check all required fields");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Author tempAuthor = this.authorRepository.findById(book.getAuthor().getId()).orElse(null);
        Publisher tempPublisher = this.publisherRepository.findById(book.getPublisher().getId()).orElse(null);

        if(tempAuthor == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No authors matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if(tempPublisher == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No publishers matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        book.setAuthor(tempAuthor);
        book.setPublisher(tempPublisher);

        Book createdBook = this.bookRepository.save(book);

        BookResponse bookResponse = new BookResponse();
        bookResponse.set(createdBook);
        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateBookById(@PathVariable int id, @RequestBody Book book) {
        if (book.getTitle() == null || book.getGenre() == null ||
                book.getAuthor() == null || book.getPublisher() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not update the book's details, please check all required fields");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Author tempAuthor = this.authorRepository.findById(book.getAuthor().getId()).orElse(null);
        Publisher tempPublisher = this.publisherRepository.findById(book.getPublisher().getId()).orElse(null);

        if(tempAuthor == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No authors matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if(tempPublisher == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No publishers matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        book.setAuthor(tempAuthor);
        book.setPublisher(tempPublisher);

        Book bookToUpdate = this.bookRepository.findById(id).orElse(null);

        if(bookToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No books matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setGenre(book.getGenre());
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setPublisher(book.getPublisher());


        Book alteredBook = this.bookRepository.save(bookToUpdate);
        BookResponse bookResponse = new BookResponse();
        bookResponse.set(alteredBook);
        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteBookById(@PathVariable int id) {
        Book bookToDelete = this.bookRepository.findById(id).orElse(null);

        if (bookToDelete == null ) {
            ErrorResponse error = new ErrorResponse();
            error.set("No books matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        this.bookRepository.delete(bookToDelete);
        BookResponse bookResponse = new BookResponse();
        bookResponse.set(bookToDelete);
        return ResponseEntity.ok(bookResponse);
    }
}
