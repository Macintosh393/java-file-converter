package com.kosobutskyi.converter.cli;

import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.generator.FileGenerator;
import com.kosobutskyi.converter.generator.GeneratorType;
import com.kosobutskyi.converter.parser.CSVParser;
import com.kosobutskyi.converter.parser.FileParser;
import com.kosobutskyi.converter.parser.ParserType;
import com.kosobutskyi.converter.utils.FileHelper;

import java.io.File;

public class Controller {
    public static void convert(String inputFileName, String outputFileName, String headers) {
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        FileParser parser = ParserType.ofType(FileHelper.getExtension(inputFileName)).get();
        if (headers != null) {
            if (parser instanceof CSVParser) {
                ((CSVParser) parser).setHeaders(headers.split(","));
            }
        }

        FileGenerator generator = GeneratorType.ofType(FileHelper.getExtension(outputFileName)).get();
        ParsedDTO data = parser.parse(inputFile);
        generator.generate(outputFile, data);
    }
}
