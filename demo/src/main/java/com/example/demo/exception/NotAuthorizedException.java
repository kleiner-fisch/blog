package com.example.demo.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Indicates that the authentificated user is not authorized to perform the action") 
public class NotAuthorizedException extends RuntimeException{

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    
}