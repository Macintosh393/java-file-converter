package com.kosobutskyi.converter.generator;

import java.util.Optional;

public enum GeneratorType {
    JSON("json", new JSONGenerator()),
    CSV("csv", new CSVGenerator()),
    XML("xml", new XMLGenerator());

    private final String extension;
    private final FileGenerator generator;

    GeneratorType(String extension, FileGenerator generator) {
        this.extension = extension;
        this.generator = generator;
    }

    public static Optional<FileGenerator> ofType(String ext) {
        for (GeneratorType generatorType : GeneratorType.values()) {
            if (generatorType.extension.equals(ext)) {
                return Optional.of(generatorType.generator);
            }
        }
        return Optional.empty();
    }

}
