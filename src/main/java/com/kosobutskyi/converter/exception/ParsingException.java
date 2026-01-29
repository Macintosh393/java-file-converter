package com.kosobutskyi.converter.exception;

public class ParsingException extends FileConversionException {
    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}