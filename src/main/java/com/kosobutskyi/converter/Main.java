package com.kosobutskyi.converter;

import com.kosobutskyi.converter.cli.Controller;
import com.kosobutskyi.converter.utils.InputValidator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> argsArray = new ArrayList<>(List.of(args));
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
    }
}