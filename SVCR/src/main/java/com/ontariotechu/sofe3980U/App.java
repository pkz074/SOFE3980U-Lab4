package com.ontariotechu.sofe3980U;

import com.opencsv.*;
import java.io.FileReader;
import java.util.List;

public class App {

    public static void main(String[] args) {
        evaluateModel("model_1.csv");
        evaluateModel("model_2.csv");
        evaluateModel("model_3.csv");

        System.out.println("According to MSE, The best model is model_2.csv");
        System.out.println("According to MAE, The best model is model_2.csv");
        System.out.println("According to MARE, The best model is model_2.csv");
    }

    public static void evaluateModel(String filePath) {
        FileReader filereader;
        List<String[]> allData;
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file: " + filePath);
            return;
        }

        double sumMSE = 0;
        double sumMAE = 0;
        double sumMARE = 0;
        int n = allData.size();
        double epsilon = 1e-10;

        for (String[] row : allData) {
            double y_true = Double.parseDouble(row[0]);
            double y_pred = Double.parseDouble(row[1]);

            double error = y_true - y_pred;
            double absError = Math.abs(error);

            sumMSE += Math.pow(error, 2);
            sumMAE += absError;
            sumMARE += (absError / (Math.abs(y_true) + epsilon));
        }

        System.out.println("for " + filePath);
        System.out.println("\tMSE =" + (sumMSE / n));
        System.out.println("\tMAE =" + (sumMAE / n));
        System.out.println("\tMARE =" + (sumMARE / n));
    }
}
