package com.dataanalysis.graphs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.dataanalysis.csv.ColumnMetadata;
import com.dataanalysis.csv.ColumnMetadataTypes;

public class FeatureTargetGraphs {
    public static void createGraphs(Map<String, List<Object>> columnData, String target, ColumnMetadata metadata) {
        for (String feature : columnData.keySet()) {
            if (feature.equals(target)) {
                continue;
            }
            if (metadata.getColumnType(feature) == ColumnMetadataTypes.NUMERICAL) {
                createScatterPlot(columnData, target, feature);
            } else if (metadata.getColumnType(feature) == ColumnMetadataTypes.CATEGORICAL) {
                createBarPlot(columnData, target, feature);
            }
        }
    }

    private static void createScatterPlot(Map<String, List<Object>> columnData, String target, String feature) {
        XYSeries series = createScatterPlotSeries(columnData.get(feature), columnData.get(target), feature, target);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createScatterPlot(feature + " vs " + target, feature, target, dataset, PlotOrientation.VERTICAL, true, true, false);
        showChart(chart);
    }

    private static XYSeries createScatterPlotSeries(List<Object> featureValues, List<Object> targetValues, String feature, String target) {
        XYSeries series = new XYSeries(feature + " vs " + target);
        for (int i = 0; i < featureValues.size(); i++) {
            try {
                double featureVal = Double.parseDouble((String) featureValues.get(i));
                double targetVal = Double.parseDouble((String) targetValues.get(i));
                series.add(featureVal, targetVal);
            } catch (NumberFormatException e) {
                System.err.println("NFE in createScatterPlot");
                System.err.println("Record: " + i);
            }
        }
        return series;
    }

    private static void createBarPlot(Map<String, List<Object>> columnData, String target, String feature) {
        DefaultCategoryDataset dataset = createBarPlotDataset(columnData.get(feature), columnData.get(target), feature, target);
        JFreeChart chart = ChartFactory.createBarChart(feature + " vs " + target, feature, "Average " + target, dataset, PlotOrientation.VERTICAL, true, true, false);
        showChart(chart);
    }

    private static DefaultCategoryDataset createBarPlotDataset(List<Object> featureValues, List<Object> targetValues, String feature, String target) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> categorySums = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (int i = 0; i < featureValues.size(); i++) {
            try {
                String category = (String) featureValues.get(i);
                double targetValue = Double.parseDouble((String) targetValues.get(i));
                categorySums.put(category, categorySums.getOrDefault(category, 0.0) + targetValue);
                categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
            } catch (NumberFormatException e) {
                System.err.println("NFE in createBarPlot");
                System.err.println("Record: " + i);
            }
        }

        for (String category : categorySums.keySet()) {
            double avg = categorySums.get(category) / categoryCounts.get(category);
            dataset.addValue(avg, "Average " + target, category);
        }

        return dataset;
    }

    private static void showChart(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame jFrame = new JFrame("chart");
        jFrame.getContentPane().add(chartPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
