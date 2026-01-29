package com.kosobutskyi.converter.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.GenerationException;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class CSVGenerator extends FileGenerator {
    @Override
    public void generate(File outputFile, ParsedDTO data) throws GenerationException {
        JsonNode rootNode = data.data();
        Set<String> headers = new LinkedHashSet<>();

        for (JsonNode childNode : rootNode) {
            Iterator<String> fieldNames = childNode.fieldNames();
            while (fieldNames.hasNext()) {
                headers.add(fieldNames.next());
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
            String[] headersArray = headers.toArray(new String[0]);
            writer.writeNext(headersArray);

            for (JsonNode childNode : rootNode) {
                String[] rowData = new String[headersArray.length];

                for (int i = 0; i < headersArray.length; i++) {
                    String key = headersArray[i];
                    JsonNode valueNode = childNode.get(key);

                    String value;

                    if (valueNode == null || valueNode.isNull()) {
                        value = "";
                    } else if (valueNode.isArray() || valueNode.isObject()) {
                        value = valueNode.toString().replace("\"", "'");
                    } else {
                        value = valueNode.asText();
                    }

                    rowData[i] = value;
                }

                writer.writeNext(rowData);
            }


        } catch (IOException e) {
            throw new GenerationException("Failed to generate CSV file: " + e.getMessage(), e);
        }
    }
}