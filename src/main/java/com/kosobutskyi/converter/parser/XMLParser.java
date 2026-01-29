package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.InvalidFileFormatException;
import com.kosobutskyi.converter.exception.ParsingException;
import com.kosobutskyi.converter.utils.XmlValidator;

import java.io.File;
import java.io.IOException;

public class XMLParser extends FileParser {
    @Override
    public ParsedDTO parse(File inputFile) throws ParsingException {
        try {
            XmlValidator.isXmlWellFormed(inputFile.getPath());

            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(inputFile);
            rootNode = parseArray(rootNode);
            return new ParsedDTO(rootNode);
        } catch (IOException e) {
            throw new ParsingException("Failed to parse XML file: " + e.getMessage(), e);
        } catch (InvalidFileFormatException e) {
            throw new ParsingException("Invalid XML format: " + e.getMessage(), e);
        }
    }

    public static JsonNode parseArray(JsonNode parentNode) {
        if (parentNode.isArray()) {
            return parentNode;
        } else if (parentNode.isObject() && parentNode.size() == 1) {
            JsonNode child = parentNode.elements().next();
            return parseArray(child);
        } else {
            return parentNode;
        }
    }
}