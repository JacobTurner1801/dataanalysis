package com.dataanalysis.csv;

import java.util.List;
import java.util.Map;

import com.dataanalysis.graphs.FeatureTargetGraphs;

public class CSVAnalyser {
    private String targetCol;
    private Map<String, List<Object>> columnsMap;
    private ColumnMetadata columnTypes;

    public CSVAnalyser(String targetCol, Map<String, List<Object>> columnsMap, ColumnMetadata columnTypes) {
        this.columnsMap = columnsMap;
        this.targetCol = targetCol;
        this.columnTypes = columnTypes;
    }

    public void createFeatureTargetGraphs() {
        FeatureTargetGraphs.createGraphs(columnsMap, targetCol, columnTypes);
    }
}
