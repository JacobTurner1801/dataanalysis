package com.dataanalysis.csv;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {
    private String filePath;
    private CSVRecord firstRecord;

    public CSVReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the csv file, including getting the header and the format
     * @return list of records for the csv file
     * @throws Exception
     */
    public List<CSVRecord> load() throws Exception {
        FileReader reader = new FileReader(this.filePath);
        boolean hasHeader = this.hasHeader(reader);
        reader = new FileReader(filePath); // reset reader
        CSVFormat format = this.createCSVFormat(hasHeader, this.firstRecord);
        CSVParser parser = new CSVParser(reader, format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();
        reader.close();
        return records;
    }

    /**
     * Creates a map of column -> list of values, potentially easier to work with.
     * Includes Target Column
     * @param records
     * @return - map of columnName -> List of values
     */
    public Map<String, List<Object>> loadAllColumnData(List<CSVRecord> records) {
        Map<String, List<Object>> columnMap = new HashMap<>();
        if (records == null || records.isEmpty()) {
            return columnMap;
        }
        CSVRecord headerRecord = records.get(0);

        for (String header : headerRecord.getParser().getHeaderNames()) {
            columnMap.put(header, new ArrayList<Object>());
        }

        for (CSVRecord record : records) {
            for (String header : record.getParser().getHeaderNames()) {
                columnMap.get(header).add((Object) record.get(header));
            }
        }

        return columnMap;
    }

    /**
     * Creates the column -> value map format based on the first record
     * @param hasHeader - whether or not the first record defines the columns or not
     * @param firstRecord - actual first record of the csv (might be a header)
     * @return - loaded csv format
     */
    private CSVFormat createCSVFormat(boolean hasHeader, CSVRecord firstRecord) {
        CSVFormat format;
        if (hasHeader) {
            format = CSVFormat.DEFAULT.builder().setHeader(firstRecord.values()).build();
        } else {
            System.err.println("You have not defined a header, so there is no target to go against, you should define a header");
            String[] header = generateDefaultHeader(firstRecord.size());
            format = CSVFormat.DEFAULT.builder().setHeader(header).build();
        }
        return format;
    }

    /**
     * Whether or not a csv file has a header
     * @param reader
     * @return if csv file has header
     * @throws Exception - can throw IOException or NoSuchElementException
     * @implNote - closes the reader given to it, so you have to create it again afterwards
     */
    private boolean hasHeader(FileReader reader) throws Exception {
        CSVParser firstPassParser = new CSVParser(reader, CSVFormat.DEFAULT);
        this.firstRecord = getFirstRecord(firstPassParser);
        boolean hasHeader = false;
        // note: may be able to use firstRecord.isMapped() somehow
        for (String val : firstRecord) {
            try {
                Double.parseDouble(val);
            } catch (NumberFormatException e) {
                hasHeader = true;
                break;
            }
        }
        firstPassParser.close();
        reader.close();
        return hasHeader;
    }

    private CSVRecord getFirstRecord(CSVParser parser) throws Exception {
        CSVRecord firstRecord = parser.iterator().next();
        return firstRecord;
    }

    private String[] generateDefaultHeader(int columns) {
        String[] header = new String[columns];
        for (int i = 0; i < columns; i++) {
            header[i] = "Column" + (i+1);
        }
        return header;
    }
}
