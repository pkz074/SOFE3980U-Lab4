package com.ontariotechu.sofe3980U;

import com.opencsv.*;
import java.io.FileReader;
import java.util.List;

public class App {

    public static void main(String[] args) {
        String filePath = "model.csv";

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
        if (n == 0) {
            System.out.println("No data found.");
            return;
        }

        int[][] confusion = new int[5][5];

        double ceSum = 0.0;
        double epsilon = 1e-15;

        for (String[] row : allData) {
            int yTrue = Integer.parseInt(row[0]);
            int trueIdx = yTrue - 1; // 0-based

            double[] probs = new double[5];
            for (int j = 0; j < 5; j++) {
                probs[j] = Double.parseDouble(row[j + 1]);
            }

            double pTrue = probs[trueIdx];
            ceSum += Math.log(pTrue + epsilon);

            int predIdx = 0;
            double maxProb = probs[0];
            for (int j = 1; j < 5; j++) {
                if (probs[j] > maxProb) {
                    maxProb = probs[j];
                    predIdx = j;
                }
            }

            confusion[predIdx][trueIdx]++;
        }

        double ce = -ceSum / n;

        System.out.printf("CE =%.7f%n", ce);
        System.out.println("Confusion matrix");
        System.out.println(
            "                y=1     y=2     y=3     y=4     y=5"
        );

        String[] labels = { "y^=1", "y^=2", "y^=3", "y^=4", "y^=5" };

        for (int i = 0; i < 5; i++) {
            System.out.printf("        %s   ", labels[i]);
            for (int j = 0; j < 5; j++) {
                System.out.printf("%-7d ", confusion[i][j]);
            }
            System.out.println();
        }
    }
}
