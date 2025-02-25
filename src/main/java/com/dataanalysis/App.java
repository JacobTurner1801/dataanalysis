package com.dataanalysis;

import java.io.FileReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class App {
    public static void main( String[] args ) throws Exception {
        for (int i = 0; i < args.length; i++) {
            System.out.println(i + " " + args[i]);
        }
        String filePath = args[0];
        String targetColumn = args[1];

        FileReader reader = new FileReader(filePath);
        CSVParser firstPassParser = new CSVParser(reader, CSVFormat.DEFAULT);
        CSVRecord firstRecord = firstPassParser.iterator().next();
        firstPassParser.close();
        reader.close();

        boolean hasHeader = false;
        for (String val : firstRecord) {
            try {
                Double.parseDouble(val);
            } catch (NumberFormatException e) {
                hasHeader = true;
                break;
            }
        }

        reader = new FileReader(filePath);
        CSVFormat format;
        if (hasHeader) {
            format = CSVFormat.DEFAULT.builder().setHeader(firstRecord.values()).build();
        } else {
            String[] header = generateDefaultHeader(firstRecord.size());
            format = CSVFormat.DEFAULT.builder().setHeader(header).build();
        }
        CSVParser actualParser = new CSVParser(reader, format);

        for (CSVRecord record : actualParser) {
            System.out.println(record.get(targetColumn));
        }
        actualParser.close();
        reader.close();
    }

    private static String[] generateDefaultHeader(int columns) {
        String[] header = new String[columns];
        for (int i = 0; i < columns; i++) {
            header[i] = "Column" + (i+1);
        }
        return header;
    }
}
