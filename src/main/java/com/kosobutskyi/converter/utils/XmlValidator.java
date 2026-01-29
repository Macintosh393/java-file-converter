package com.kosobutskyi.converter.utils;

import com.kosobutskyi.converter.exception.InvalidFileFormatException;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlValidator {
    public static void isXmlWellFormed(String filePath) throws InvalidFileFormatException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new File(filePath));
        } catch (SAXException e) {
            throw new InvalidFileFormatException("XML is not well-formed: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new InvalidFileFormatException("Error reading XML file: " + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            throw new InvalidFileFormatException("Parser configuration error: " + e.getMessage(), e);
        }
    }
}