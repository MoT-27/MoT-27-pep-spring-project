package com.example.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String exceptionMessage){
        super(exceptionMessage);
    }
}
