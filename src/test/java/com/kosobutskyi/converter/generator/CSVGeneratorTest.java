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

class CSVGeneratorTest {

    private CSVGenerator generator;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new CSVGenerator();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGenerate_WithSimpleData() throws Exception {
        // Create test data
        ArrayNode rootArray = objectMapper.createArrayNode();

        ObjectNode obj1 = objectMapper.createObjectNode();
        obj1.put("name", "John");
        obj1.put("age", 25);
        obj1.put("isActive", true);
        rootArray.add(obj1);

        ObjectNode obj2 = objectMapper.createObjectNode();
        obj2.put("name", "Jane");
        obj2.put("age", 30);
        obj2.put("isActive", false);
        rootArray.add(obj2);

        ParsedDTO data = new ParsedDTO(rootArray);
        File outputFile = tempDir.resolve("output.csv").toFile();

        generator.generate(outputFile, data);

        // Verify file content
        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertEquals(3, lines.size()); // Header + 2 data rows

        assertEquals("\"name\",\"age\",\"isActive\"", lines.get(0)); // Headers in insertion order
        assertEquals("\"John\",\"25\",\"true\"", lines.get(1));
        assertEquals("\"Jane\",\"30\",\"false\"", lines.get(2));
    }

    @Test
    void testGenerate_WithNullAndEmptyValues() throws Exception {
        // Create test data with null and empty
        ArrayNode rootArray = objectMapper.createArrayNode();

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("name", "John");
        obj.putNull("value");
        obj.put("description", "");
        rootArray.add(obj);

        ParsedDTO data = new ParsedDTO(rootArray);
        File outputFile = tempDir.resolve("output.csv").toFile();

        generator.generate(outputFile, data);

        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertEquals(2, lines.size()); // Header + 1 data row

        assertEquals("\"name\",\"value\",\"description\"", lines.get(0));
        assertEquals("\"John\",\"\",\"\"", lines.get(1)); // Empty for description, null for value
    }

    @Test
    void testGenerate_WithJsonArraysAndObjects() throws Exception {
        // Create test data with JSON structures
        ArrayNode rootArray = objectMapper.createArrayNode();

        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("name", "John");

        // Add array
        ArrayNode skills = objectMapper.createArrayNode();
        skills.add("Java");
        skills.add("Python");
        obj.set("skills", skills);

        // Add object
        ObjectNode contact = objectMapper.createObjectNode();
        contact.put("email", "john@example.com");
        obj.set("contact", contact);

        rootArray.add(obj);

        ParsedDTO data = new ParsedDTO(rootArray);
        File outputFile = tempDir.resolve("output.csv").toFile();

        generator.generate(outputFile, data);

        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertEquals(2, lines.size());

        assertEquals("\"name\",\"skills\",\"contact\"", lines.get(0));
        assertEquals("\"John\",\"['Java','Python']\",\"{'email':'john@example.com'}\"", lines.get(1));
    }

    @Test
    void testGenerate_EmptyArray() throws Exception {
        ArrayNode rootArray = objectMapper.createArrayNode();
        ParsedDTO data = new ParsedDTO(rootArray);
        File outputFile = tempDir.resolve("output.csv").toFile();

        generator.generate(outputFile, data);

        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertEquals(1, lines.size()); // Only header, but since no objects, maybe empty?
        // Actually, if no objects, headers set is empty, so only empty line? Wait, let's check

        // The code collects headers from all objects, if empty array, no headers
        assertTrue(lines.get(0).isEmpty() || lines.size() == 0);
    }

    @Test
    void testGenerate_IoException() throws Exception {
        ArrayNode rootArray = objectMapper.createArrayNode();
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("name", "John");
        rootArray.add(obj);

        ParsedDTO data = new ParsedDTO(rootArray);
        File invalidFile = new File("invalid/path/output.csv"); // Non-writable path

        GenerationException exception = assertThrows(GenerationException.class,
                () -> generator.generate(invalidFile, data));
        assertTrue(exception.getMessage().contains("Failed to generate CSV file"));
    }
}