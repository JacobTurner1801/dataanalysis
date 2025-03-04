package com.dataanalysis.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartPanel;

import com.dataanalysis.graphs.FeatureTargetGraphs;
import com.dataanalysis.metrics.Correlation;

public class CSVAnalyser {
    private String targetCol;
    private Map<String, List<Object>> columnsMap;
    private ColumnMetadata columnTypes;

    public CSVAnalyser(String targetCol, Map<String, List<Object>> columnsMap, ColumnMetadata columnTypes) {
        this.columnsMap = columnsMap;
        this.targetCol = targetCol;
        this.columnTypes = columnTypes;
    }

    public List<ChartPanel> createFeatureTargetGraphs() {
        return FeatureTargetGraphs.createGraphsPanel(columnsMap, targetCol, columnTypes);
    }

    public List<Double> getAllNumericalCorrelations() {
        List<Double> correlations = new ArrayList<>();
        for (String col : this.columnsMap.keySet()) {
            // TODO: target column might be categorical, but this isn't taken into account yet
            if (columnTypes.getColumnType(col) == ColumnMetadataTypes.NUMERICAL) {
                correlations.add(getNumericalCorrelationScore(col));
            }
        }
        return correlations;
    }

    public List<Map<String, Double>> getCategoricalNumericalCorrelations() {
        List<Map<String, Double>> categoryMaps = new ArrayList<>();
        for (String col : columnsMap.keySet()) {
            if (columnTypes.getColumnType(col) == ColumnMetadataTypes.CATEGORICAL) {
                categoryMaps.add(getCategoricalToNumericalCorrelationScores(col));
            }
        }
        return categoryMaps;
    }

    private double getNumericalCorrelationScore(String feature) {
        return Correlation.correlation(columnsMap.get(targetCol), columnsMap.get(feature));
    }

    private Map<String, Double> getCategoricalToNumericalCorrelationScores(String feature) {
        return Correlation.categoricalCorrelation(columnsMap, targetCol, feature);
    }
}
