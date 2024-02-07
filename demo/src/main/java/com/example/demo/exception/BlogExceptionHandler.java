package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class BlogExceptionHandler {
    
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e){
        BlogException newException = new BlogException(e.getMessage(), e.getCause(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(newException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ResponseEntity<Object> handleCommentNotFoundException(CommentNotFoundException e){
        BlogException newException = new BlogException(e.getMessage(), e.getCause(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(newException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {PostNotFoundException.class})
    public ResponseEntity<Object> handlePostNotFoundException(PostNotFoundException e){
        BlogException newException = new BlogException(e.getMessage(), e.getCause(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(newException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handlePostNotFoundException(ConstraintViolationException e){
        BlogException newException = new BlogException(e.getMessage(), e.getCause(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newException, HttpStatus.BAD_REQUEST);
    }
}
