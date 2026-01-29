package com.kosobutskyi.converter.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.GenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONGeneratorTest {

    private JSONGenerator generator;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new JSONGenerator();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGenerate_WithArrayData() throws Exception {
        // Create test data
        ArrayNode rootArray = objectMapper.createArrayNode();

        ObjectNode obj1 = objectMapper.createObjectNode();
        obj1.put("name", "John");
        obj1.put("age", 25);
        obj1.put("active", true);
        rootArray.add(obj1);

        ObjectNode obj2 = objectMapper.createObjectNode();
        obj2.put("name", "Jane");
        obj2.put("age", 30);
        obj2.put("active", false);
        rootArray.add(obj2);

        ParsedDTO data = new ParsedDTO(rootArray);
        File outputFile = tempDir.resolve("output.json").toFile();

        generator.generate(outputFile, data);

        // Verify file content
        String content = Files.readString(outputFile.toPath());
        assertNotNull(content);
        assertTrue(content.contains("\"name\" : \"John\""));
        assertTrue(content.contains("\"age\" : 25"));
        assertTrue(content.contains("\"active\" : true"));
        assertTrue(content.contains("\"name\" : \"Jane\""));
        assertTrue(content.contains("\"age\" : 30"));
        assertTrue(content.contains("\"active\" : false"));
    }

    @Test
    void testGenerate_WithObjectData() throws Exception {
        // Create test data as object (should still work)
        ObjectNode rootObject = objectMapper.createObjectNode();
        rootObject.put("name", "John");
        rootObject.put("age", 25);

        ParsedDTO data = new ParsedDTO(rootObject);
        File outputFile = tempDir.resolve("output.json").toFile();

        generator.generate(outputFile, data);

        String content = Files.readString(outputFile.toPath());
        assertNotNull(content);
        assertTrue(content.contains("\"name\" : \"John\""));
        assertTrue(content.contains("\"age\" : 25"));
    }

    @Test
    void testGenerate_IoException() throws Exception {
        ArrayNode rootArray = objectMapper.createArrayNode();
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("name", "John");
        rootArray.add(obj);

        ParsedDTO data = new ParsedDTO(rootArray);
        File invalidFile = new File("invalid/path/output.json"); // Non-writable path

        GenerationException exception = assertThrows(GenerationException.class,
                () -> generator.generate(invalidFile, data));
        assertTrue(exception.getMessage().contains("Failed to generate JSON file"));
    }
}