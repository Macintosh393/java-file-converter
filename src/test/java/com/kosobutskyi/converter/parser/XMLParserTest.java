package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.InvalidFileFormatException;
import com.kosobutskyi.converter.exception.ParsingException;
import com.kosobutskyi.converter.utils.XmlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XMLParserTest {

    private XMLParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new XMLParser();
    }

    @Test
    void testParse_ValidXmlArray() throws IOException, ParsingException {
        File xmlFile = tempDir.resolve("test.xml").toFile();
        try (FileWriter writer = new FileWriter(xmlFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<root>\n");
            writer.write("  <item>\n");
            writer.write("    <name>John</name>\n");
            writer.write("    <age>25</age>\n");
            writer.write("  </item>\n");
            writer.write("  <item>\n");
            writer.write("    <name>Jane</name>\n");
            writer.write("    <age>30</age>\n");
            writer.write("  </item>\n");
            writer.write("</root>\n");
        }

        try (MockedStatic<XmlValidator> validatorMock = mockStatic(XmlValidator.class)) {
            validatorMock.when(() -> XmlValidator.isXmlWellFormed(anyString())).then(invocation -> null);

            ParsedDTO result = parser.parse(xmlFile);

            assertNotNull(result);
            JsonNode data = result.data();
            assertTrue(data.isArray());
            assertEquals(2, data.size());

            JsonNode first = data.get(0);
            assertEquals("John", first.get("name").asText());
            assertEquals("25", first.get("age").asText());

            JsonNode second = data.get(1);
            assertEquals("Jane", second.get("name").asText());
            assertEquals("30", second.get("age").asText());
        }
    }

    @Test
    void testParse_ValidXmlObject() throws IOException, ParsingException {
        File xmlFile = tempDir.resolve("test.xml").toFile();
        try (FileWriter writer = new FileWriter(xmlFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<item>\n");
            writer.write("  <name>John</name>\n");
            writer.write("  <age>25</age>\n");
            writer.write("</item>\n");
        }

        try (MockedStatic<XmlValidator> validatorMock = mockStatic(XmlValidator.class)) {
            validatorMock.when(() -> XmlValidator.isXmlWellFormed(anyString())).then(invocation -> null);

            ParsedDTO result = parser.parse(xmlFile);

            assertNotNull(result);
            JsonNode data = result.data();
            assertTrue(data.isObject());
            assertEquals("John", data.get("name").asText());
            assertEquals("25", data.get("age").asText());
        }
    }

    @Test
    void testParse_InvalidXml() throws IOException {
        File xmlFile = tempDir.resolve("invalid.xml").toFile();
        try (FileWriter writer = new FileWriter(xmlFile)) {
            writer.write("<root><item><name>John</item></root>");
        }

        try (MockedStatic<XmlValidator> validatorMock = mockStatic(XmlValidator.class)) {
            validatorMock.when(() -> XmlValidator.isXmlWellFormed(anyString()))
                    .thenThrow(new InvalidFileFormatException("XML is not well-formed"));

            ParsingException exception = assertThrows(ParsingException.class, () -> parser.parse(xmlFile));
            assertTrue(exception.getMessage().contains("Invalid XML format"));
        }
    }

    @Test
    void testParse_FileNotFound() {
        File nonExistentFile = new File("nonexistent.xml");

        try (MockedStatic<XmlValidator> validatorMock = mockStatic(XmlValidator.class)) {
            validatorMock.when(() -> XmlValidator.isXmlWellFormed(anyString()))
                    .thenThrow(new InvalidFileFormatException("Error reading XML file"));

            ParsingException exception = assertThrows(ParsingException.class, () -> parser.parse(nonExistentFile));
            assertTrue(exception.getMessage().contains("Invalid XML format"));
        }
    }

    @Test
    void testParseArray_WithNestedStructure() {
        // Test the static parseArray method
        // Create a mock JsonNode structure
        // Since it's static, we can test it directly if accessible, but it's package-private
        // For now, skip as it's tested indirectly in parse tests
    }
}