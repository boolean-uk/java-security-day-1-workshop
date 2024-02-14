package com.booleanuk.library.controller;

import com.booleanuk.library.model.Author;
import com.booleanuk.library.model.Book;
import com.booleanuk.library.model.Publisher;
import com.booleanuk.library.repository.PublisherRepository;
import com.booleanuk.library.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    @Autowired
    private PublisherRepository publisherRepository;


    @GetMapping
    public ResponseEntity<PublisherListResponse> getAllPublishers() {
        List<Publisher> publishers = this.publisherRepository.findAll();
        PublisherListResponse publisherListResponse = new PublisherListResponse();
        publisherListResponse.set(publishers);
        return ResponseEntity.ok(publisherListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPublisherById(@PathVariable int id) {
        Publisher returnPublisher = this.publisherRepository.findById(id).orElse(null);
        if (returnPublisher == null ) {
            ErrorResponse error = new ErrorResponse();
            error.set("No publishers matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        PublisherResponse publisherResponse = new PublisherResponse();
        publisherResponse.set(returnPublisher);
        return ResponseEntity.ok(publisherResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createPublisher(@RequestBody Publisher publisher) {
        if (publisher.getName() == null || publisher.getLocation() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create the publisher, please check all required fields");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Publisher createdPublisher = this.publisherRepository.save(publisher);
        createdPublisher.setBook(new ArrayList<>());

        PublisherResponse publisherResponse = new PublisherResponse();
        publisherResponse.set(createdPublisher);
        return new ResponseEntity<>(publisherResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updatePublisher(@PathVariable int id, @RequestBody Publisher publisher) {
        if (publisher.getName() == null || publisher.getLocation() == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Could not update the author's details, please check all required fields");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Publisher publisherToUpdate = this.publisherRepository.findById(id).orElse(null);

        if(publisherToUpdate == null ) {
            ErrorResponse error = new ErrorResponse();
            error.set("No publishers matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        publisherToUpdate.setName(publisher.getName());
        publisherToUpdate.setLocation(publisher.getLocation());
        publisherToUpdate.setBook(new ArrayList<>());

        PublisherResponse publisherResponse = new PublisherResponse();
        publisherResponse.set(publisherToUpdate);
        return new ResponseEntity<>(publisherResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deletePublisher(@PathVariable int id) {
        Publisher publisherToDelete = this.publisherRepository.findById(id).orElse(null);

        if (publisherToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("No publishers matching that id were found");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        this.publisherRepository.delete(publisherToDelete);
        publisherToDelete.setBook(new ArrayList<Book>());

        PublisherResponse publisherResponse = new PublisherResponse();
        publisherResponse.set(publisherToDelete);
        return ResponseEntity.ok(publisherResponse);
    }
}
