package com.kosobutskyi.converter.parser;

import com.kosobutskyi.converter.dto.ParsedDTO;
import com.kosobutskyi.converter.exception.ParsingException;

import java.io.File;

public abstract class FileParser {
    public abstract ParsedDTO parse(File inputFile) throws ParsingException;
}
