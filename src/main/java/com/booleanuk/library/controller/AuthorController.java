package com.booleanuk.library.controller;

import com.booleanuk.library.model.Author;
import com.booleanuk.library.model.Book;
import com.booleanuk.library.repository.AuthorRepository;
import com.booleanuk.library.response.AuthorListResponse;
import com.booleanuk.library.response.AuthorResponse;
import com.booleanuk.library.response.ErrorResponse;
import com.booleanuk.library.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
    public ResponseEntity<AuthorListResponse> getAllAuthors() {
        List<Author> authors = this.authorRepository.findAll();
        AuthorListResponse authorListResponse = new AuthorListResponse();
        authorListResponse.set(authors);
        return ResponseEntity.ok(authorListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getAuthorById(@PathVariable int id) {
        Author returnAuthor = this.authorRepository.findById(id).orElse(null);
        if (returnAuthor == null ) {
            ErrorResponse error = new ErrorResponse();
            error.set("No authors matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.set(returnAuthor);
        return ResponseEntity.ok(authorResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createAuthor(@RequestBody Author author) {
        if (author.getFirst_name() == null || author.getLast_name() == null || author.getEmail() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create the author, please check all required fields");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Author createdAuthor = this.authorRepository.save(author);
        createdAuthor.setBook(new ArrayList<>());

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.set(createdAuthor);
        return new ResponseEntity<>(authorResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateAuthor(@PathVariable int id, @RequestBody Author author) {
        if (author.getFirst_name() == null || author.getLast_name() == null || author.getEmail() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not update the author's details, please check all required fields");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Author authorToUpdate = this.authorRepository.findById(id).orElse(null);

        if(authorToUpdate == null ) {
            ErrorResponse error = new ErrorResponse();
            error.set("No authors matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        authorToUpdate.setFirst_name(author.getFirst_name());
        authorToUpdate.setLast_name(author.getLast_name());
        authorToUpdate.setEmail(author.getEmail());
        authorToUpdate.setAlive(author.isAlive());
        authorToUpdate.setBook(new ArrayList<>());

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.set(authorToUpdate);
        return new ResponseEntity<>(authorResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteAuthor(@PathVariable int id) {
        Author authorToDelete = this.authorRepository.findById(id).orElse(null);

        if(authorToDelete == null ) {
            ErrorResponse error = new ErrorResponse();
            error.set("No authors matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        this.authorRepository.delete(authorToDelete);
        authorToDelete.setBook(new ArrayList<Book>());

        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.set(authorToDelete);
        return ResponseEntity.ok(authorResponse);
    }
}
