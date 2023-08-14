package com.edts.concerts.utils.exceptions;

public class CustomsException extends RuntimeException{

    public CustomsException(String message) {
        super(message);
    }

    public CustomsException(String message, Throwable cause) {
        super(message, cause);
    }

}
