package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class BlogException {
    private final String message;
    private final Throwable cause;
    private final HttpStatus statusCode;


    public BlogException(String message, Throwable cause, HttpStatus statusCode) {
        this.message = message;
        this.cause = cause;
        this.statusCode = statusCode;
    }
    
        
    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

}
