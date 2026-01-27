package com.kosobutskyi.converter.generator;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosobutskyi.converter.dto.ParsedDTO;

import java.io.File;
import java.io.IOException;

public class JSONGenerator extends FileGenerator {
    @Override
    public void generate(File outputFile, ParsedDTO data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, data.data());
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
