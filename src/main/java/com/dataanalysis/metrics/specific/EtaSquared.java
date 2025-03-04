package com.dataanalysis.metrics.specific;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class EtaSquared {
    public static double calculateEtaSquared(List<double[]> data) {
        double sst = calculateTotalSumOfSquares(data);
        double ssb = calculateBetweenSumOfSquares(data);
        double etaSquared = ssb / sst;
        return etaSquared;
    }

    private static double calculateTotalSumOfSquares(List<double[]> data) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        data.forEach(group -> {
           for (double val : group) {
            stats.addValue(val);
           }
        });
        double mean = stats.getMean();
        double n = stats.getN(); // number of values
        return stats.getSumsq() - n * Math.pow(mean, 2);
    }

    private static double calculateBetweenSumOfSquares(List<double[]> data) {
        DescriptiveStatistics overallStats = new DescriptiveStatistics();
        List<Double> groupMeans = new ArrayList<>();
        List<Long> groupSizes = new ArrayList<>();

        // go through each double[] and get group mean and group size
        // also add each individual double val to overallStats
        data.forEach(group -> {
            DescriptiveStatistics groupSpecificStats = new DescriptiveStatistics();
            for (double val : group) {
                overallStats.addValue(val);
                groupSpecificStats.addValue(val);
            }
            groupMeans.add(groupSpecificStats.getMean());
            groupSizes.add(groupSpecificStats.getN());
        });

        double overallMean = overallStats.getMean();
        double ssb = 0.0;
        for (int i = 0; i < data.size(); i++) {
            ssb += groupSizes.get(i) * Math.pow(groupMeans.get(i) - overallMean, 2);
        }
        return ssb;
    }
}
