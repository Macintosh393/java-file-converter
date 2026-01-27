package com.kosobutskyi.converter.generator;

import com.kosobutskyi.converter.dto.ParsedDTO;

import java.io.File;

public abstract class FileGenerator {
    public abstract void generate(File outputFile, ParsedDTO data);
}
