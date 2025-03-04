package com.dataanalysis.csv;

import java.util.List;
import java.util.Map;

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

    public void createFeatureTargetGraphs() {
        FeatureTargetGraphs.createGraphs(columnsMap, targetCol, columnTypes);
    }

    public double getNumericalCorrelationScore(String feature) {
        // TODO: target column might be categorical, but this isn't taken into account yet
        if (columnTypes.getColumnType(feature) == ColumnMetadataTypes.NUMERICAL) {
            return Correlation.correlation(columnsMap.get(targetCol), columnsMap.get(feature));
        }
        return 0.0;
    }

    public Map<String, Double> getCategoricalToNumericalCorrelationScores(String feature) {
        return Correlation.categoricalCorrelation(columnsMap, targetCol, feature);
    }
}
