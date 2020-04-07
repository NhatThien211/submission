package com.practicalexam.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckingUtils {
    private static String PROJECT_DIR = System.getProperty("user.dir");
    private static String STUDENT_PATH = PROJECT_DIR + File.separator
            + "src" + File.separator
            + "main" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "practicalexam" + File.separator
            + "student";
    private static String evaluatingLog = PROJECT_DIR + File.separator + "evaluating.log";

    public static void changeValueOfFile(Map<String, String> maps) {
        try (FileWriter writer = new FileWriter(PROJECT_DIR + File.separator + "testdata.txt", false)) {
            String s = "";
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                s += entry.getKey() + ":" + entry.getValue() + "-";
            }
            writer.write(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static boolean checkConsoleLogContains(String s, String method) {
        List<String> content = null;
        try {
            content = Files.readAllLines(Paths.get(evaluatingLog));
            for (int i = 0; i < content.size(); i++) {
                if (content.get(i).contains(s)) {
                    if (content.get(i + 1).contains(method)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {}
        return false;
    }

    public static void changeScannerToValue(List<String> variables) {
        Charset charset = StandardCharsets.UTF_8;
        List<File> studentCodeFiles = new ArrayList<>();
        getAllFiles(STUDENT_PATH, studentCodeFiles, ".java");
        if (studentCodeFiles.size() > 0) {
            for (File file : studentCodeFiles) {
                try {
                    String result = "";
                    Path path = Paths.get(file.getAbsolutePath());
                    List<String> content = null;
                    try {
                        content = Files.readAllLines(path);
                        for (int i = 0; i < content.size(); i++) {
                            String line = content.get(i);
                            for (String variable : variables) {
                                if (line.toLowerCase().contains(variable.toLowerCase())) {
                                    line = line.replaceAll("scannerObj\\.nextInt\\(\\)", "DBManager.getValue(\"" + variable + "\")")
                                            .replaceAll("scannerObj\\.nextDouble\\(\\)", "DBManager.getValue(\"" + variable + "\")")
                                            .replaceAll("scannerObj\\.nextFloat\\(\\)", "DBManager.getValue(\"" + variable + "\")")
                                            .replaceAll("scannerObj\\.nextBoolean\\(\\)", "true")
                                            .replaceAll("scannerObj\\.nextLine\\(\\)", "DBManager.getValue(\"" + variable + "\")");
                                }
                            }
                            result += line+"\n";
                        }
                    } catch (IOException e) {
                    }
                    Files.write(path, result.getBytes(charset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void getAllFiles(String directoryName, List<File> files, String extension) {
        // Get all files from a directory.
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().endsWith(extension)) {
                        files.add(file);
                    }
                } else if (file.isDirectory()) {
                    getAllFiles(file.getAbsolutePath(), files, extension);
                }
            }
    }
}
