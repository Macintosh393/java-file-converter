package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;

import java.io.File;
import java.io.IOException;

public class XMLParser extends FileParser {
    @Override
    public ParsedDTO parse(File inputFile) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(inputFile);
            return new ParsedDTO(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
