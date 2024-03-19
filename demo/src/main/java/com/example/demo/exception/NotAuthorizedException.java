package com.example.demo.exception;

public class NotAuthorizedException extends RuntimeException{

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    
}