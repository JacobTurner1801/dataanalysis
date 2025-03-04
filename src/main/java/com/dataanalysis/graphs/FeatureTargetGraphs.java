package com.dataanalysis.graphs;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static List<ChartPanel> createGraphsPanel(Map<String, List<Object>> columnData, String target, ColumnMetadata metadata) {
        List<ChartPanel> chartPanels = new ArrayList<>();

        for (String feature : columnData.keySet()) {
            if (feature.equals(target)) {
                continue;
            }
            ChartPanel graphPanel = null;
            if (metadata.getColumnType(feature) == ColumnMetadataTypes.NUMERICAL) {
                graphPanel = createScatterPlot(columnData, target, feature);
            } else if (metadata.getColumnType(feature) == ColumnMetadataTypes.CATEGORICAL) {
                graphPanel = createBarPlot(columnData, target, feature);
            }
            graphPanel.setPreferredSize(new Dimension(300, 1080 / columnData.keySet().size()));
            if (graphPanel != null) {
                chartPanels.add(graphPanel);
            }
        }

        return chartPanels;
    }

    private static ChartPanel createScatterPlot(Map<String, List<Object>> columnData, String target, String feature) {
        XYSeries series = createScatterPlotSeries(columnData.get(feature), columnData.get(target), feature, target);
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createScatterPlot(feature + " vs " + target, feature, target, dataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(chart);
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

    private static ChartPanel createBarPlot(Map<String, List<Object>> columnData, String target, String feature) {
        DefaultCategoryDataset dataset = createBarPlotDataset(columnData.get(feature), columnData.get(target), feature, target);
        JFreeChart chart = ChartFactory.createBarChart(feature + " vs " + target, feature, "Average " + target, dataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(chart);
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
}
