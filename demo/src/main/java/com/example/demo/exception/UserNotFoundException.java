package com.example.demo.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Indicates that a user could not be found") 
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    
}