package com.dataanalysis;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVRecord;

import com.dataanalysis.csv.CSVReader;

public class App {
    public static void main( String[] args ) {
        // for (int i = 0; i < args.length; i++) {
        //     System.out.println(i + " " + args[i]);
        // }
        String filePath = args[0];
        String targetColumn = args[1];

        CSVReader csvReader = new CSVReader(filePath);
        try {
            List<CSVRecord> records = csvReader.load();
            Map<String, List<Object>> allDataMap = csvReader.loadAllColumnData(records);
            // Iterator<Entry<String, List<Object>>> it = columnMap.entrySet().iterator();
            // while (it.hasNext()) {
            //     Entry<String, List<Object>> e = it.next();
            //     System.out.println(e.getKey());
            //     System.out.println(e.getValue().size());
            // }
            System.out.println("total records: " + records.size());
            System.out.println("loaded");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
