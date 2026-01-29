package com.kosobutskyi.converter;

import com.kosobutskyi.converter.cli.Controller;
import com.kosobutskyi.converter.exception.FileConversionException;
import com.kosobutskyi.converter.exception.InputValidationException;
import com.kosobutskyi.converter.utils.InputValidator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<String> argsArray = new ArrayList<>(List.of(args));

            if (argsArray.contains("--help")) {
                System.out.println("Usage: java -jar file-converter.jar --input <input-file> --output <output-file> [--csv-mapping <[h1,h2,h3,...]>]");
                System.out.println("Optional flag --csv-mapping [array of headers] for CSV files with no headers");
                System.exit(0);
            }

            InputValidator.validate(argsArray);

            int inputComIdx = argsArray.indexOf("--input");
            int outputComIdx = argsArray.indexOf("--output");
            int mappingArrIdx = argsArray.indexOf("--csv-mapping");

            String inputFileName = argsArray.get(inputComIdx + 1);
            String outputFileName = argsArray.get(outputComIdx + 1);
            String mappingHeaders;

            if (mappingArrIdx != -1) {
                mappingHeaders = argsArray.get(mappingArrIdx + 1);
                mappingHeaders = mappingHeaders.substring(1, mappingHeaders.length() - 1);
            } else {
                mappingHeaders = null;
            }

            Controller.convert(inputFileName, outputFileName, mappingHeaders);
            System.out.println("File conversion completed successfully.");
        } catch (InputValidationException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Usage: java -jar file-converter.jar --input <input-file> --output <output-file> [--csv-mapping [header1,header2,...]]");
            System.exit(1);
        } catch (FileConversionException e) {
            System.err.println("Conversion failed: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            System.exit(1);
        }
    }
}