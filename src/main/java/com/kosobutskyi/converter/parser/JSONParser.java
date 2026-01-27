package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;

import java.io.File;
import java.io.IOException;

public class JSONParser extends FileParser {
    @Override
    public ParsedDTO parse(File inputFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputFile);

            if (!rootNode.isArray()) {
                throw new IllegalArgumentException("Input should be an array of objects");
            }

            return new ParsedDTO(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
