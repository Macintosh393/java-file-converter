package com.kosobutskyi.converter.cli;

import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.generator.FileGenerator;
import com.kosobutskyi.converter.generator.GeneratorType;
import com.kosobutskyi.converter.parser.FileParser;
import com.kosobutskyi.converter.parser.ParserType;
import com.kosobutskyi.converter.utils.FileHelper;

import java.io.File;

public class Controller {
    public static void main(String[] args) {
        String inputFileName = "data/input.xml";
        String outputFileName = "data/output.csv";

        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        FileParser parser = ParserType.ofType(FileHelper.getExtension(inputFileName)).get();
        FileGenerator generator = GeneratorType.ofType(FileHelper.getExtension(outputFileName)).get();
        ParsedDTO data = parser.parse(inputFile);
        generator.generate(outputFile, data);
    }
}
