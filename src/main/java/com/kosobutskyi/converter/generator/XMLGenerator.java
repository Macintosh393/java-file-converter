package com.kosobutskyi.converter.generator;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.kosobutskyi.converter.dto.ParsedDTO;

import java.io.File;
import java.io.IOException;

public class XMLGenerator extends FileGenerator {
    @Override
    public void generate(File outputFile, ParsedDTO data) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            ObjectMapper jsonMapper = new ObjectMapper();

            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, false);

            JsonNode rootNode = data.data();

            if (rootNode.isArray()) {
                ObjectNode wrapper = jsonMapper.createObjectNode();
                wrapper.set("object", rootNode);
                rootNode = wrapper;
            }

            xmlMapper.writer().withDefaultPrettyPrinter().withRootName("Root").writeValue(outputFile, rootNode);
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
