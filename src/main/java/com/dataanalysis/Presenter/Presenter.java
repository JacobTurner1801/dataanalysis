package com.dataanalysis.Presenter;

import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
        mainContent.add(constructNumericalList(numericalCorrs));
        mainContent.add(constructCategoricalNumericalTextArea(categoricalCorrs));
        mainContent.setLayout(new GridLayout((int) Math.ceil(Math.sqrt(featureTargetCharts.size() * numericalCorrs.size() * categoricalCorrs.size())), (int) Math.ceil(Math.sqrt(featureTargetCharts.size() * numericalCorrs.size() * categoricalCorrs.size()))));

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

        frame.getContentPane().add(scrollPane);
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

    private JList<JTextArea> constructCategoricalNumericalTextArea(List<Map<String, Double>> correlations) {
        DefaultListModel<JTextArea> model = new DefaultListModel<>();
        for (Map<String, Double> map : correlations) {
            JTextArea ta = createTextArea(map);
            ta.setEditable(false);
            model.addElement(ta);
        }
        return new JList<>(model);
    }

    private JTextArea createTextArea(Map<String, Double> map) {
        StringBuilder text = new StringBuilder();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            text.append(entry.getKey() + ": " + entry.getValue().toString());
        }
        return new JTextArea(text.toString());
    }
}
