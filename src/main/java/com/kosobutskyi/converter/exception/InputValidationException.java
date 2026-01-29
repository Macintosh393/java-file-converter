package com.kosobutskyi.converter.exception;

public class InputValidationException extends FileConversionException {
    public InputValidationException(String message) {
        super(message);
    }

    public InputValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}