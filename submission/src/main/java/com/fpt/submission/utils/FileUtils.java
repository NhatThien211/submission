package com.fpt.submission.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

public class FileUtils {
    public static void appendStringToResultFile(String studentCode, Map<String, Integer> testResultsStatus) {
        File file = null;
        PrintWriter writer = null;
        try {
            file = new File("Result.txt");
            writer = new PrintWriter(new FileWriter(file, true));
            double mark = 0;
            String resultText = "";
            if (studentCode != null) {
                resultText += studentCode;
            } else {
                for (Map.Entry<String, Integer> entry : testResultsStatus.entrySet()) {
                    if (entry.getValue() == 1) {
                        resultText += entry.getKey() + ": Passed + \n";
                        mark += 2.5;
                    } else {
                        resultText += entry.getKey() + ": Failed + \n";
                    }
                }
                resultText += "Time : " + TimeUtils.getCurTime() + "\n";
                resultText += "Result : " + TimeUtils.getCurTime() + "\n";

            }
            writer.println(resultText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }


    }
}
