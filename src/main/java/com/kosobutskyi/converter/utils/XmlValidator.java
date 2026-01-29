package com.kosobutskyi.converter.utils;

import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XmlValidator {
    public static void isXmlWellFormed(String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new File(filePath));
            System.out.println("XML file is well-formed.");
        } catch (SAXException e) {
            System.err.println("XML is not well-formed: " + e.getMessage());
            throw new RuntimeException();
        } catch (IOException e) {
            System.err.println("Error reading XML file: " + e.getMessage());
            throw new RuntimeException();
        } catch (ParserConfigurationException e) {
            System.err.println("Parser configuration error: " + e.getMessage());
            throw new RuntimeException();
        }
    }
}
