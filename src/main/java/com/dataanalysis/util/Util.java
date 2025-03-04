package com.dataanalysis.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Util {
    public static void printOutMapValues(Map<String, Double> data) {
        Iterator<Entry<String, Double>> iterator = data.entrySet().iterator();
        if (!iterator.hasNext()) {
            return;
        }
        while (iterator.hasNext()) {
            Entry<String, Double> current = iterator.next();
            System.out.println("Key: " + current.getKey() + "Val: " + current.getValue());
        }
    }
}
