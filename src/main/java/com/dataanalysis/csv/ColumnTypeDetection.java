package com.dataanalysis.csv;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColumnTypeDetection {

    /**
     * Try and detect the metadata types automatically
     * The user should check if the types are correct themselves
     * @param columnData
     * @return
     */
    public static ColumnMetadata setMetadataTypes(Map<String, List<Object>> columnData) {
        ColumnMetadata metadata = new ColumnMetadata();
        for (String feature : columnData.keySet()) {
            if (isNumerical(columnData.get(feature))) {
                metadata.setColumnType(feature, ColumnMetadataTypes.NUMERICAL);
            } else {
                metadata.setColumnType(feature, ColumnMetadataTypes.CATEGORICAL);
            }
        }
        return metadata;
    }

    private static boolean isNumerical(List<Object> data) {
        Set<Object> uniqueVals =  new HashSet<>();
        for (Object val : data) {
            if (val != null) {
                uniqueVals.add(val);
            }
        }

        if (uniqueVals.size() == 0 || uniqueVals.size() < 8) {
            return false;
        }

        int parses = 0;
        for (Object val : uniqueVals) {
            try {
                Double.parseDouble((String) val);
                parses++;
            } catch (NumberFormatException e) {
                System.err.println("NFE in isNumerical");
                System.err.println("Val: " + val);
                System.err.println("Parses until then: " + parses);
            }
        }
        return (double) parses / uniqueVals.size() > 0.85;
    }
}
