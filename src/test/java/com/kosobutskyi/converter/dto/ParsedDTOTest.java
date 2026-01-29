package com.kosobutskyi.converter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParsedDTOTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testParsedDTO_Creation() throws Exception {
        JsonNode testNode = objectMapper.readTree("{\"test\": \"value\"}");

        ParsedDTO dto = new ParsedDTO(testNode);

        assertNotNull(dto);
        assertEquals(testNode, dto.data());
    }

    @Test
    void testParsedDTO_Equality() throws Exception {
        JsonNode node1 = objectMapper.readTree("{\"key\": \"value\"}");
        JsonNode node2 = objectMapper.readTree("{\"key\": \"value\"}");

        ParsedDTO dto1 = new ParsedDTO(node1);
        ParsedDTO dto2 = new ParsedDTO(node2);

        assertEquals(dto1, dto2);
    }

    @Test
    void testParsedDTO_HashCode() throws Exception {
        JsonNode node = objectMapper.readTree("{\"test\": 123}");

        ParsedDTO dto = new ParsedDTO(node);

        assertNotNull(dto.hashCode());
    }

    @Test
    void testParsedDTO_ToString() throws Exception {
        JsonNode node = objectMapper.readTree("[1, 2, 3]");

        ParsedDTO dto = new ParsedDTO(node);

        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("ParsedDTO"));
    }
}