package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.InvalidFileFormatException;
import com.kosobutskyi.converter.exception.ParsingException;

import java.io.File;
import java.io.IOException;

public class JSONParser extends FileParser {
    @Override
    public ParsedDTO parse(File inputFile) throws ParsingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputFile);

            if (!rootNode.isArray()) {
                throw new InvalidFileFormatException("Input JSON should be an array of objects");
            }

            return new ParsedDTO(rootNode);
        } catch (JsonProcessingException e) {
            throw new ParsingException("Invalid JSON format: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ParsingException("Failed to read JSON file: " + e.getMessage(), e);
        }
    }
}