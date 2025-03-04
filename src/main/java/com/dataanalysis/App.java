package com.dataanalysis;

import com.dataanalysis.Analyser.DataAnalyser;
import com.dataanalysis.Presenter.Presenter;

public class App {
    public static void main( String[] args ) {
        // for (int i = 0; i < args.length; i++) {
        //     System.out.println(i + " " + args[i]);
        // }
        String filePath = args[0];
        String targetColumn = args[1];

        try {
            DataAnalyser dataAnalyser = new DataAnalyser(filePath, targetColumn);
            new Presenter(dataAnalyser);
        } catch (Exception e) {
            System.err.println("Error in main");
            e.printStackTrace();
        }
    }
}
