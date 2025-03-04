package com.dataanalysis.metrics;

import java.util.List;

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
}
