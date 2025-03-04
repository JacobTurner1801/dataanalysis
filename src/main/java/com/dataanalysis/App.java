package com.dataanalysis;

import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import com.dataanalysis.csv.CSVAnalyser;
import com.dataanalysis.csv.CSVReader;
import com.dataanalysis.csv.ColumnMetadata;
import com.dataanalysis.csv.ColumnMetadataTypes;
import com.dataanalysis.csv.ColumnTypeDetection;

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
            Map<String, List<Object>> allDataMap = csvReader.loadAllColumnData(records.subList(1, records.size()));
            System.out.println("total records: " + records.size());
            System.out.println("loaded");
            ColumnMetadata metadata = ColumnTypeDetection.setMetadataTypes(allDataMap);
            metadata.showTypes();
            CSVAnalyser analyser = new CSVAnalyser(targetColumn, allDataMap, metadata);
            analyser.createFeatureTargetGraphs();
            for (String col : allDataMap.keySet()) {
                if (col.equals(targetColumn) || metadata.getColumnType(col) == ColumnMetadataTypes.CATEGORICAL) {
                    continue;
                }
                System.out.println(col + " to price: " + analyser.getCorrelationScores(targetColumn, col));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
