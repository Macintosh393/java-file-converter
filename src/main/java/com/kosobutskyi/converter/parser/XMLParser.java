package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
            rootNode = parseArray(rootNode);
            return new ParsedDTO(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode parseArray(JsonNode parentNode) {
        if (!parentNode.isArray()) {
            for (JsonNode child : parentNode) {
                parentNode = parseArray(child);
            }
        }
        return parentNode;
    }
}
