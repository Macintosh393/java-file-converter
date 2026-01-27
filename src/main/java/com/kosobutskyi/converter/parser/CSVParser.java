package com.kosobutskyi.converter.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVParser extends FileParser {

    @Override
    public ParsedDTO parse(File inputFile) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode rootArray = mapper.createArrayNode();

        try (CSVReader reader = new CSVReader(new FileReader(inputFile))) {
            List<String[]> allRows = reader.readAll();

            if (allRows.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty");
            }

            String[] headers = allRows.get(0);

            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                ObjectNode objectNode = mapper.createObjectNode();

                for (int j = 0; j < headers.length; j++) {
                    String key = headers[j];
                    String value = (j < headers.length) ? row[j] : "";

                    addTypedValue(objectNode, key, value);
                }

                rootArray.add(objectNode);
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }

        return new ParsedDTO(rootArray);
    }

    private static void addTypedValue(ObjectNode node, String key, String value) {
        if (value == null || value.isEmpty()) {
            node.putNull(key);
            return;
        }

        // Try Boolean
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            node.put(key, Boolean.parseBoolean(value));
            return;
        }

        // Try Integer/Long
        try {
            long l = Long.parseLong(value);
            node.put(key, l);
            return;
        } catch (NumberFormatException ignored) {}

        // Try Double
        try {
            double d = Double.parseDouble(value);
            node.put(key, d);
            return;
        } catch (NumberFormatException ignored) {}

        // Try Complex JSON (Array/Object) stored as String
        // Example: value is "['Java','Python']" -> convert back to real JSON array
        if ((value.startsWith("{") && value.endsWith("}")) ||
                (value.startsWith("[") && value.endsWith("]"))) {
            try {
                // If it parses successfully, add it as a JSON tree, not a string!
                ObjectMapper subMapper = new ObjectMapper();
                // Handle the single quotes we might have swapped in earlier
                String validJson = value.replace("'", "\"");
                node.set(key, subMapper.readTree(validJson));
                return;
            } catch (Exception ignored) {
                // Not valid JSON, treat as normal string
            }
        }

        // Fallback: It's just a String
        node.put(key, value);
    }
}
