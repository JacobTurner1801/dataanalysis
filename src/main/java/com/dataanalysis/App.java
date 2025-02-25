package com.dataanalysis;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.dataanalysis.csv.CSVReader;

public class App {
    public static void main( String[] args ) throws Exception {
        // for (int i = 0; i < args.length; i++) {
        //     System.out.println(i + " " + args[i]);
        // }
        String filePath = args[0];
        String targetColumn = args[1];

        CSVReader csvReader = new CSVReader(filePath);
        try {
            List<CSVRecord> records = csvReader.load();
            System.out.println("total records: " + records.size());
            System.out.println("loaded");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
