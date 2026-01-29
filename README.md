# Multi-Format File Converter

A robust Java-based command-line utility for bidirectional conversion between **JSON**, **XML**, and **CSV** formats. Built with clean code principles and design patterns to ensure high performance and maintainability.

## Capabilities
- **Bidirectional Conversion**: Seamlessly convert between JSON, XML, and CSV.
- **Deep Parsing**: Correctly handles nested structures and arrays within JSON and XML files.
- **Data Integrity**: Preserves field mapping and data accuracy across all conversion paths.
- **Input Validation**: Automatically validates the structural correctness of input files before processing.
- **Robust Error Handling**: Provides clear, user-friendly feedback for incorrect CLI arguments or corrupted files.

## Usage Guide
The application is executed as a standalone JAR file. Ensure you have Java 23 or higher installed.

### Command Syntax
```bash
java -jar file-converter.jar --input <input-file> --output <output-file> [--csv-mapping [header1,header2,...]]
```

### Parameters
- `--input <path>`: Path to the source file (required).
- `--output <path>`: Path where the converted file will be saved (required).
- `--csv-mapping [<headers>]`: Optional. Provides headers for CSV files that do not contain a header row. Format: `["col1","col2",...]`.

---

## Conversion Examples

### 1. CSV to JSON Conversion
Convert a CSV file with data to a structured JSON file.
```bash
java -jar target/file-converter-1.0-SNAPSHOT.jar --input data/input.csv --output data/output.json
```

**Before (CSV):**
```csv
"id","name","isActive","age","skills","contact","notes"
"101","Max Mustermann","true","29","['Java','Spring Boot','Docker']","{'email':'max@example.com','phone':'+1-555-0100'}","Standard profile."
"102","Vanya Ivanov","false","22","['JavaScript','React']","{'email':'vanya@test.org'}","He said: ""I love coding in Java"" which might break CSV if not escaped."
"103","Alex Smith","true","34","","",""
```

**After (JSON):**
```json
[ {
  "id" : 101,
  "name" : "Max Mustermann",
  "isActive" : true,
  "age" : 29,
  "skills" : [ "Java", "Spring Boot", "Docker" ],
  "contact" : {
    "email" : "max@example.com",
    "phone" : "+1-555-0100"
  },
  "notes" : "Standard profile."
}, {
  "id" : 102,
  "name" : "Vanya Ivanov",
  "isActive" : false,
  "age" : 22,
  "skills" : [ "JavaScript", "React" ],
  "contact" : {
    "email" : "vanya@test.org"
  },
  "notes" : "He said: \"I love coding in Java\" which might break CSV if not escaped."
}, {
  "id" : 103,
  "name" : "Alex Smith",
  "isActive" : true,
  "age" : 34,
  "skills" : null,
  "contact" : null,
  "notes" : null
} ]
```

### 2. JSON to XML Conversion
Convert a nested JSON object into a valid XML format.
```bash
java -jar target/file-converter-1.0-SNAPSHOT.jar --input data/input.json --output data/output.xml
```

**Before (JSON):**
```json
{
  "id": 101,
  "name": "Max Mustermann",
  "isActive": true,
  "age": 29,
  "skills": ["Java", "Spring Boot", "Docker"],
  "contact": {
    "email": "max@example.com",
    "phone": "+1-555-0100"
  }
}
```

**After (XML):**
```xml
<Root>
  <id>101</id>
  <name>Max Mustermann</name>
  <isActive>true</isActive>
  <age>29</age>
  <skills>Java</skills>
  <skills>Spring Boot</skills>
  <skills>Docker</skills>
  <contact>
    <email>max@example.com</email>
    <phone>+1-555-0100</phone>
  </contact>
</Root>
```

### 3. XML to CSV Conversion
Flatten an XML structure into a comma-separated values (CSV) file.
```bash
java -jar target/file-converter-1.0-SNAPSHOT.jar --input data/input.xml --output data/output.csv
```

**Before (XML):**
```xml
<Root>
    <object>
        <id>101</id>
        <name>Max Mustermann</name>
        <isActive>true</isActive>
        <skills>Java</skills>
        <skills>Spring Boot</skills>
    </object>
    <object>
        <id>102</id>
        <name>Vanya Ivanov</name>
        <isActive>false</isActive>
        <skills>JavaScript</skills>
    </object>
</Root>
```

**After (CSV):**
```csv
"id","name","isActive","skills"
"101","Max Mustermann","true","['Java','Spring Boot']"
"102","Vanya Ivanov","false","['JavaScript']"
```

---

## Technical Details
- **Architecture**: Implements Strategy and Factory design patterns for extensible parser and generator logic.
- **Validation**: Uses custom validators to ensure XML schema compliance and structural integrity.
- **Testing**: Comprehensive unit tests covering key parsing and generation scenarios.
