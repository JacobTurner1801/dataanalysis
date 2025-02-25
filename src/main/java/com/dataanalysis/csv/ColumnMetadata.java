package com.dataanalysis.csv;

import java.util.HashMap;
import java.util.Map;

public class ColumnMetadata {
    private Map<String, Boolean> columnTypes = new HashMap<>();
    public ColumnMetadata() {}

    public void setColumnType(String name, boolean type) {
        columnTypes.put(name, type);
    }

    public boolean getColumnType(String name) {
        return columnTypes.get(name);
    }

    public void setColumnTypesMap(HashMap<String, Boolean> map) {
        this.columnTypes.putAll(map);
    }

    public Map<String, Boolean> getColumnTypes() {
        return columnTypes;
    }
}
