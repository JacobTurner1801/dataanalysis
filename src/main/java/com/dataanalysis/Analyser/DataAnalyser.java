package com.dataanalysis.Analyser;

import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartPanel;

import com.dataanalysis.csv.CSVAnalyser;
import com.dataanalysis.csv.CSVReader;
import com.dataanalysis.csv.ColumnMetadata;
import com.dataanalysis.csv.ColumnTypeDetection;

public class DataAnalyser {
    private String csvFilePath;
    private String targetColumn;
    private CSVReader reader;
    private List<CSVRecord> records;
    private Map<String, List<Object>> columnData;
    private ColumnMetadata metadata;
    private CSVAnalyser analyser;

    public DataAnalyser(String csvFilePath, String targetColumn) throws Exception {
        this.csvFilePath = csvFilePath;
        this.targetColumn = targetColumn;
        this.reader = new CSVReader(this.csvFilePath);
        this.records = this.reader.load();
        this.columnData = loadColumnMap();
        this.metadata = ColumnTypeDetection.setMetadataTypes(this.columnData);
        this.analyser = new CSVAnalyser(this.targetColumn, this.columnData, this.metadata);
    }

    private Map<String, List<Object>> loadColumnMap() {
        return this.reader.loadAllColumnData(this.records.subList(1, this.records.size()));
    }

    public List<ChartPanel> createFeatureTargetGraphs() {
        return this.analyser.createFeatureTargetGraphs();
    }

    public List<Double> getNumericalCorrelations() {
        return this.analyser.getAllNumericalCorrelations();
    }

    public List<Map<String, Double>> getCategoricalCorrelations() {
        return this.analyser.getCategoricalNumericalCorrelations();
    }
}
