package com.kosobutskyi.converter.parser;



import java.util.Optional;

public enum ParserType {
    JSON("json", new JSONParser()),
    CSV("csv", new CSVParser()),
    XML("xml", new XMLParser());

    private final String extension;
    private final FileParser parser;

    ParserType(String extension, FileParser parser) {
        this.extension = extension;
        this.parser = parser;
    }

    public static Optional<FileParser> ofType(String ext) {
        for (ParserType parserType : ParserType.values()) {
            if (parserType.extension.equals(ext)) {
                return Optional.of(parserType.parser);
            }
        }
        return Optional.empty();
    }
}
