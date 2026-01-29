package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.InvalidFileFormatException;
import com.kosobutskyi.converter.exception.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CSVParserTest {

    private CSVParser parser;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new CSVParser();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testParse_WithHeadersInFile() throws IOException, ParsingException {
        File csvFile = tempDir.resolve("test.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,age,isActive\n");
            writer.write("John,25,true\n");
            writer.write("Jane,30,false\n");
        }

        ParsedDTO result = parser.parse(csvFile);

        assertNotNull(result);
        JsonNode data = result.data();
        assertTrue(data.isArray());
        assertEquals(2, data.size());

        JsonNode first = data.get(0);
        assertEquals("John", first.get("name").asText());
        assertEquals(25, first.get("age").asInt());
        assertTrue(first.get("isActive").asBoolean());

        JsonNode second = data.get(1);
        assertEquals("Jane", second.get("name").asText());
        assertEquals(30, second.get("age").asInt());
        assertFalse(second.get("isActive").asBoolean());
    }

    @Test
    void testParse_WithCustomHeaders() throws IOException, ParsingException {
        File csvFile = tempDir.resolve("test.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("John,25,true\n");
            writer.write("Jane,30,false\n");
        }

        parser.setHeaders(new String[]{"name", "age", "isActive"});
        ParsedDTO result = parser.parse(csvFile);

        assertNotNull(result);
        JsonNode data = result.data();
        assertTrue(data.isArray());
        assertEquals(2, data.size());

        JsonNode first = data.get(0);
        assertEquals("John", first.get("name").asText());
        assertEquals(25, first.get("age").asInt());
        assertTrue(first.get("isActive").asBoolean());
    }

    @Test
    void testParse_EmptyFile() throws IOException {
        File csvFile = tempDir.resolve("empty.csv").toFile();
        csvFile.createNewFile(); // Empty file

        ParsingException exception = assertThrows(ParsingException.class, () -> parser.parse(csvFile));
        assertTrue(exception.getCause() instanceof InvalidFileFormatException);
        assertEquals("CSV file is empty", exception.getCause().getMessage());
    }

    @Test
    void testParse_FileNotFound() {
        File nonExistentFile = new File("nonexistent.csv");

        ParsingException exception = assertThrows(ParsingException.class, () -> parser.parse(nonExistentFile));
        assertTrue(exception.getMessage().contains("CSV file not found"));
    }

    @Test
    void testParse_WithJsonStringValues() throws IOException, ParsingException {
        File csvFile = tempDir.resolve("test.json.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,skills\n");
            writer.write("John,\"['Java','Python']\"\n");
        }

        ParsedDTO result = parser.parse(csvFile);

        assertNotNull(result);
        JsonNode data = result.data();
        assertTrue(data.isArray());
        assertEquals(1, data.size());

        JsonNode first = data.get(0);
        assertEquals("John", first.get("name").asText());
        JsonNode skills = first.get("skills");
        assertTrue(skills.isArray());
        assertEquals("Java", skills.get(0).asText());
        assertEquals("Python", skills.get(1).asText());
    }

    @Test
    void testParse_WithNullAndEmptyValues() throws IOException, ParsingException {
        File csvFile = tempDir.resolve("test.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("name,value\n");
            writer.write("John,\n");
            writer.write(",empty\n");
        }

        ParsedDTO result = parser.parse(csvFile);

        assertNotNull(result);
        JsonNode data = result.data();
        assertTrue(data.isArray());
        assertEquals(2, data.size());

        JsonNode first = data.get(0);
        assertEquals("John", first.get("name").asText());
        assertTrue(first.get("value").isNull());

        JsonNode second = data.get(1);
        assertTrue(second.get("name").isNull());
        assertEquals("empty", second.get("value").asText());
    }
}