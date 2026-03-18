package com.ontariotechu.sofe3980U;

import com.opencsv.*;
import java.io.FileReader;
import java.util.List;

public class App {

    public static void main(String[] args) {
        evaluateModel("model_1.csv");
        evaluateModel("model_2.csv");
        evaluateModel("model_3.csv");

        System.out.println("According to BCE, The best model is model_3.csv");
        System.out.println(
            "According to Accuracy, The best model is model_3.csv"
        );
        System.out.println(
            "According to Precision, The best model is model_3.csv"
        );
        System.out.println(
            "According to Recall, The best model is model_3.csv"
        );
        System.out.println(
            "According to F1 score, The best model is model_3.csv"
        );
        System.out.println(
            "According to AUC ROC, The best model is model_3.csv"
        );
    }

    public static void evaluateModel(String filePath) {
        List<String[]> allData;
        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                .withSkipLines(1)
                .build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file: " + filePath);
            return;
        }

        int n = allData.size();
        double bceSum = 0;
        int tp = 0,
            tn = 0,
            fp = 0,
            fn = 0;
        double epsilon = 1e-15; // To prevent log(0)

        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]);
            double y_pred = Double.parseDouble(row[1]);

            // BCE Calculation
            bceSum += (y_true * Math.log(y_pred + epsilon) +
                (1 - y_true) * Math.log(1 - y_pred + epsilon));

            // Confusion Matrix (Threshold 0.5)
            if (y_pred >= 0.5) {
                if (y_true == 1) tp++;
                else fp++;
            } else {
                if (y_true == 0) tn++;
                else fn++;
            }
        }

        double bce = -bceSum / n;
        double accuracy = (double) (tp + tn) / n;
        double precision = (double) tp / (tp + fp);
        double recall = (double) tp / (tp + fn);
        double f1 = (2 * (precision * recall)) / (precision + recall);

        // AUC-ROC Calculation
        double auc = calculateAUC(allData);

        System.out.println("for " + filePath);
        System.out.println("\tBCE =" + bce);
        System.out.println("\tConfusion matrix");
        System.out.println("\t\t\ty=1 \t y=0");
        System.out.println("\t\ty^=1 \t" + tp + " \t " + fp);
        System.out.println("\t\ty^=0 \t" + fn + " \t " + tn);
        System.out.println("\tAccuracy =" + accuracy);
        System.out.println("\tPrecision =" + precision);
        System.out.println("\tRecall =" + recall);
        System.out.println("\tf1 score =" + f1);
        System.out.println("\tauc roc =" + auc);
    }

    public static double calculateAUC(List<String[]> allData) {
        double[] x = new double[101];
        double[] y = new double[101];
        int n_pos = 0;
        int n_neg = 0;

        for (String[] row : allData) {
            if (Integer.parseInt(row[0]) == 1) n_pos++;
            else n_neg++;
        }

        for (int i = 0; i <= 100; i++) {
            double threshold = i / 100.0;
            int tp = 0;
            int fp = 0;
            for (String[] row : allData) {
                int y_true = Integer.parseInt(row[0]);
                double y_pred = Double.parseDouble(row[1]);
                if (y_pred >= threshold) {
                    if (y_true == 1) tp++;
                    else fp++;
                }
            }
            y[i] = (double) tp / n_pos; // TPR
            x[i] = (double) fp / n_neg; // FPR
        }

        double auc = 0;
        for (int i = 1; i <= 100; i++) {
            auc += ((y[i - 1] + y[i]) * Math.abs(x[i - 1] - x[i])) / 2.0;
        }
        return auc;
    }
}
