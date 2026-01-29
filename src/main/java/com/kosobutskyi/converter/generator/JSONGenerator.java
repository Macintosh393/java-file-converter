package com.kosobutskyi.converter.generator;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.GenerationException;

import java.io.File;
import java.io.IOException;

public class JSONGenerator extends FileGenerator {
    @Override
    public void generate(File outputFile, ParsedDTO data) throws GenerationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, data.data());
        } catch (StreamWriteException e) {
            throw new GenerationException("Failed to write JSON file: " + e.getMessage(), e);
        } catch (DatabindException e) {
            throw new GenerationException("Invalid data for JSON generation: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new GenerationException("Failed to generate JSON file: " + e.getMessage(), e);
        }

    }
}