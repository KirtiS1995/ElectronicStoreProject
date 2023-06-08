package com.electronic.store.exceptions;

public class BadApiRequestException extends RuntimeException{

    public BadApiRequestException() {
        super("Bad Api REquest ..!");

    }

    public BadApiRequestException(String message) {
        super(message);
    }

}
