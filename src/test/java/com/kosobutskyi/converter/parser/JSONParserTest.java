package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest {

    private JSONParser parser;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        parser = new JSONParser();
    }

    @Test
    void testParse_ValidJsonArray() throws IOException, ParsingException {
        File jsonFile = tempDir.resolve("test.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[\n");
            writer.write("  {\"name\": \"John\", \"age\": 25},\n");
            writer.write("  {\"name\": \"Jane\", \"age\": 30}\n");
            writer.write("]");
        }

        ParsedDTO result = parser.parse(jsonFile);

        assertNotNull(result);
        JsonNode data = result.data();
        assertTrue(data.isArray());
        assertEquals(2, data.size());

        JsonNode first = data.get(0);
        assertEquals("John", first.get("name").asText());
        assertEquals(25, first.get("age").asInt());

        JsonNode second = data.get(1);
        assertEquals("Jane", second.get("name").asText());
        assertEquals(30, second.get("age").asInt());
    }

    @Test
    void testParse_ValidJsonObject() throws IOException, ParsingException {
        File jsonFile = tempDir.resolve("test.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("{\"name\": \"John\", \"age\": 25}");
        }

        ParsedDTO result = parser.parse(jsonFile);

        assertNotNull(result);
        JsonNode data = result.data();
        assertTrue(data.isObject());
        assertEquals("John", data.get("name").asText());
        assertEquals(25, data.get("age").asInt());
    }

    @Test
    void testParse_InvalidJson() throws IOException {
        File jsonFile = tempDir.resolve("invalid.json").toFile();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[{\"name\": \"John\", \"age\": }]");
        }

        ParsingException exception = assertThrows(ParsingException.class, () -> parser.parse(jsonFile));
        assertTrue(exception.getMessage().contains("Invalid JSON format"));
    }

    @Test
    void testParse_FileNotFound() {
        File nonExistentFile = new File("nonexistent.json");

        ParsingException exception = assertThrows(ParsingException.class, () -> parser.parse(nonExistentFile));
        assertTrue(exception.getMessage().contains("Failed to read JSON file"));
    }
}