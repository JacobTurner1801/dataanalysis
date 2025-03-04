package com.dataanalysis.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.OneWayAnova;

import com.dataanalysis.metrics.specific.EtaSquared;
public class Correlation {
    // https://stackoverflow.com/questions/28428365/how-to-find-correlation-between-two-integer-arrays-in-java#28428582
    public static double correlation(List<Object> targetValues, List<Object> featureValues) {
        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;

        int n = featureValues.size();
        for (int i = 0; i < n; i++) {
            try {
                double x = Double.parseDouble((String) featureValues.get(i));
                double y = Double.parseDouble((String) targetValues.get(i));
                sx += x;
                sy += y;
                sxx += x * x;
                syy += y * y;
                sxy += x * y;
            } catch (NumberFormatException e) {
                System.err.println("NFE in correlation, record: " + i);
                continue;
            }
        }

        double covariation = sxy / n - sx * sy / n / n;
        double sigmax = Math.sqrt(sxx / n - sx * sx / n / n);
        double sigmay = Math.sqrt(syy / n - sy * sy / n / n);
        return covariation / sigmax / sigmay;
    }

    public static Map<String, Double> categoricalCorrelation(Map<String, List<Object>> columnData, String target, String feature) {
        System.out.println("\nCategorical Feature: " + feature);
        Map<String, Double> finalStatistics = new HashMap<>();
        Map<String, List<Double>> categoryGroups = new HashMap<>();
        List<Object> targetValues = columnData.get(target);
        List<Object> categories = columnData.get(feature);
        int n = categories.size();
        for (int i = 0; i < n; i++) {
            String category = (String) categories.get(i);
            try {
                double val = Double.parseDouble((String) targetValues.get(i));
                categoryGroups.computeIfAbsent(category, k -> new ArrayList<>()).add(val);
            } catch (NumberFormatException e) {
                System.err.println("NFE in categorical correlation for category: " + category);
            }
        }

        // ANOVA
        List<double[]> data = new ArrayList<>();
        for (List<Double> group : categoryGroups.values()) {
            if (!(group.size() > 1)) {
                continue;
            }
            double[] groupArray = group.stream().mapToDouble(Double::doubleValue).toArray();
            data.add(groupArray);
        }
        try {
            OneWayAnova anova = new OneWayAnova();

            double fValue = anova.anovaFValue(data);
            finalStatistics.put("f-value", fValue);
            double pValue = anova.anovaPValue(data);
            finalStatistics.put("p-value", pValue);

            double etaSquared = EtaSquared.calculateEtaSquared(data);
            finalStatistics.put("eta-squared", etaSquared);

            for (String category : categoryGroups.keySet()) {
                DescriptiveStatistics stats = new DescriptiveStatistics();
                categoryGroups.get(category).forEach(stats::addValue);
                finalStatistics.put("mean for " + category, stats.getMean());
            }
        } catch (Exception e) {
            System.err.println("Error during Anova calculation");
            System.err.println(e.getMessage());
            Iterator<Entry<String, List<Double>>> it = categoryGroups.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, List<Double>> entry = it.next();
                System.out.println(entry.getKey() + " " + entry.getValue().size());
            }
        }
        return finalStatistics;
    }
}
