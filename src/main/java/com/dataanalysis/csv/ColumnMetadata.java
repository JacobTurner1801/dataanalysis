package com.dataanalysis.csv;

import java.util.HashMap;
import java.util.Map;

public class ColumnMetadata {
    private Map<String, ColumnMetadataTypes> columnTypes = new HashMap<>();
    public ColumnMetadata() {}

    public void setColumnType(String name, ColumnMetadataTypes type) {
        columnTypes.put(name, type);
    }
    /**
     * Gets column type
     * @param column name
     * @return type
     */
    public ColumnMetadataTypes getColumnType(String name) {
        return columnTypes.get(name);
    }

    public void setColumnTypesMap(HashMap<String, ColumnMetadataTypes> map) {
        this.columnTypes.putAll(map);
    }

    public Map<String, ColumnMetadataTypes> getColumnTypes() {
        return columnTypes;
    }

    public void showTypes() {
        for (String col : columnTypes.keySet()) {
            String after =  columnTypes.get(col) == ColumnMetadataTypes.NUMERICAL ? "Numerical"  : "Categorical";
            System.out.println(col + " " + after);
        }
    }
}
