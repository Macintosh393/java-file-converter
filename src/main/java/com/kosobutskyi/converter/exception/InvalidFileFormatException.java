package com.kosobutskyi.converter.exception;

public class InvalidFileFormatException extends ParsingException {
    public InvalidFileFormatException(String message) {
        super(message);
    }

    public InvalidFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}