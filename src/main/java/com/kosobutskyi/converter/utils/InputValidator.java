package com.kosobutskyi.converter.utils;

import com.kosobutskyi.converter.exception.InputValidationException;

import java.util.List;

public class InputValidator {
    public static void validate(List<String> args) throws InputValidationException {
        if (args.size() < 4 || args.size() > 6 || args.size() == 5) {
            throw new InputValidationException("Wrong number of parameters");
        }

        int inputComIdx = args.indexOf("--input");
        int outputComIdx = args.indexOf("--output");

        if (inputComIdx % 2 != 0 || outputComIdx % 2 != 0) {
            throw new InputValidationException("Wrong order of parameters");
        }

        if (args.size() > 4) {
            int mappingArrIdx = args.indexOf("--csv-mapping");
            if (mappingArrIdx % 2 != 0) {
                throw new InputValidationException("Wrong order of parameters");
            }

            String headersArg = args.get(mappingArrIdx + 1);
            if (!headersArg.startsWith("[") && !headersArg.endsWith("]")) {
                throw new InputValidationException("Headers provided for the CSV mapping should be written as --csv-mapping [.., .., ...]");
            };
        }
    }
}