package com.dataanalysis.Presenter;

import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartPanel;

import com.dataanalysis.Analyser.DataAnalyser;

public class Presenter {
    private DataAnalyser dataAnalyser;
    private JFrame frame;
    public Presenter(DataAnalyser dataAnalyser) {
        this.dataAnalyser = dataAnalyser;
        this.frame = new JFrame("CSV Analyser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainContent = new JPanel();

        List<ChartPanel> featureTargetCharts = this.dataAnalyser.createFeatureTargetGraphs();
        for (ChartPanel chp : featureTargetCharts) {
            mainContent.add(chp);
        }
        // System.out.println(featureTargetCharts.size());

        List<Double> numericalCorrs = this.dataAnalyser.getNumericalCorrelations();
        // System.out.println(numericalCorrs.size());
        List<Map<String, Double>> categoricalCorrs = this.dataAnalyser.getCategoricalCorrelations();
        // System.out.println(categoricalCorrs.size());
        JList<Double> numericalList = constructNumericalList(numericalCorrs);
        JList<String> categoricalList = constructCategoricalNumericalTextArea(categoricalCorrs);

        mainContent.add(numericalList);
        JScrollPane scrollPane = new JScrollPane(categoricalList);
        mainContent.add(scrollPane);
        double dim = Math.ceil(Math.sqrt(featureTargetCharts.size()) + Math.sqrt(categoricalCorrs.size()) + Math.sqrt(numericalCorrs.size()));
        // edit first param for displaying text properly :)
        mainContent.setLayout(new GridLayout((int) dim, (int) dim));

        // JScrollPane scrollPane = new JScrollPane(mainContent);
        // scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        // scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

        // frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(mainContent);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1920, 1080);
    }

    private JList<Double> constructNumericalList(List<Double> corrs) {
        DefaultListModel<Double> model = new DefaultListModel<>();
        for (double corr : corrs) {
            model.addElement(corr);
        }
        return new JList<>(model);
    }

    private JList<String> constructCategoricalNumericalTextArea(List<Map<String, Double>> correlations) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Map<String, Double> map : correlations) {
            String toDisplay = createStringFromCategoricalValues(map);
            model.addElement(toDisplay);
        }
        return new JList<>(model);
    }

    private String createStringFromCategoricalValues(Map<String, Double> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            builder.append(String.format("%s: %.2f; \r", entry.getKey(), entry.getValue()));
        }
        return builder.toString();
    }
}
