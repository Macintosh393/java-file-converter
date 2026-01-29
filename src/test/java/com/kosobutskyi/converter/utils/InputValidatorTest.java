package com.kosobutskyi.converter.utils;

import com.kosobutskyi.converter.exception.InputValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    @Test
    void testValidate_ValidArgsWithInputOutput() throws InputValidationException {
        List<String> args = Arrays.asList("--input", "input.csv", "--output", "output.json");
        assertDoesNotThrow(() -> InputValidator.validate(args));
    }

    @Test
    void testValidate_ValidArgsWithCsvMapping() throws InputValidationException {
        List<String> args = Arrays.asList("--input", "input.csv", "--output", "output.json", "--csv-mapping", "[a,b,c]");
        assertDoesNotThrow(() -> InputValidator.validate(args));
    }

    @ParameterizedTest
    @MethodSource("invalidArgsProvider")
    void testValidate_InvalidArgs(List<String> args, String expectedMessage) {
        InputValidationException exception = assertThrows(InputValidationException.class,
                () -> InputValidator.validate(args));
        assertEquals(expectedMessage, exception.getMessage());
    }

    static Stream<Arguments> invalidArgsProvider() {
        return Stream.of(
                Arguments.of(Arrays.asList("--input", "input.csv"), "Wrong number of parameters"),
                Arguments.of(Arrays.asList("--input", "input.csv", "--output", "output.json", "--extra"), "Wrong number of parameters"),
                Arguments.of(Arrays.asList("--input", "input.csv", "--output", "output.json", "--csv-mapping"), "Wrong number of parameters"),
                Arguments.of(Arrays.asList("input.csv", "--input", "--output", "output.json"), "Wrong order of parameters"),
                Arguments.of(Arrays.asList("--input", "input.csv", "output.json", "--output"), "Wrong order of parameters"),
                Arguments.of(Arrays.asList("--input", "input.csv", "--output", "output.json", "mapping", "--csv-mapping"), "Wrong order of parameters"),
                Arguments.of(Arrays.asList("--input", "input.csv", "--output", "output.json", "--csv-mapping", "a,b,c"), "Headers provided for the CSV mapping should be written as --csv-mapping [.., .., ...]")
        );
    }
}