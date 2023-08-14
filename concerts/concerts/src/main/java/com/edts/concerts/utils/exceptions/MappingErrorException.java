package com.edts.concerts.utils.exceptions;

public class MappingErrorException extends RuntimeException{

    public MappingErrorException(String message) {
        super(message);
    }

    public MappingErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
