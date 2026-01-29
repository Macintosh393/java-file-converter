package com.kosobutskyi.converter.exception;

public class GenerationException extends FileConversionException {
    public GenerationException(String message) {
        super(message);
    }

    public GenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}