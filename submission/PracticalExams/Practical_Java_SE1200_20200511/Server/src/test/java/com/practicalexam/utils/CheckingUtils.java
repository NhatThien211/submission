package com.practicalexam.utils;

import org.assertj.core.data.MapEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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


    public static void changeValueOfFile(String[] params) {
        Map<String, String> data = getDataFromFile();
        String key = "";
        String value = "";
        try (FileWriter writer = new FileWriter(PROJECT_DIR + File.separator + "testdata.txt", false)) {
            String s = "";
            for (int i = 0; i < params.length; i++) {
                key = (new ArrayList<String>(data.keySet())).get(i);
                value = (new ArrayList<String>(data.values())).get(i);
                s += key + ":" + params[i] + ":" + value + "-";
            }
            writer.write(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isPrinted(String[] s, String method, String start) {
        List<String> content;
        String temp = "";
        boolean isStarted = false;
        boolean isEnded = false;
        try {
            content = Files.readAllLines(Paths.get(evaluatingLog), StandardCharsets.UTF_8);
            for (int i = 0; i <= content.size(); i++) {
                temp = content.get(i);
                if (temp.contains(start)) isStarted = true;
                if (temp.contains(method)) isEnded = true;
                if (isStarted && !isEnded) {
                    if (temp.contains("|")) return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkConsoleLogContains(String[] s, String method, String start) {
        List<String> content;
        String temp = "";
        boolean isContain;
        boolean isInPosition = false;
        try {
            Thread.sleep(5000);
            content = Files.readAllLines(Paths.get(evaluatingLog));
            for (int i = 0; i < content.size(); i++) {
                temp = content.get(i);
                if (temp.contains(start)) isInPosition = true;
                if (temp.contains(method)) {
                    return false;
                }
                if (isInPosition) {
                    isContain = true;
                    for (String item : s) {
                        if (!temp.contains(item)) {
                            isContain = false;
                            break;
                        }
                    }
                    if (isContain) {
                        if (content.get(i + 1).contains(method)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String removeBlankInFormat(String s) {
        char[] charSet = s.toCharArray();
        String newString = "";
        for (char item : charSet) {
            if (' ' != item) {
                newString += item;
            }
        }
        return newString;
    }

    static Map<String, String> type = new HashMap<>();

    private static void addType() {
        type.clear();
        type.put("int", "Integer.parseInt(");
        type.put("double", "Double.parseDouble(");
        type.put("float", "Float.parseFloat(");
    }

    private static Map<String, String> getDataFromFile() {
        Charset charset = StandardCharsets.UTF_8;
        Map<String, String> data = new LinkedHashMap<>();
        try {
            String s = new String(Files.readAllBytes(Paths.get(PROJECT_DIR + File.separator + "testdata.txt")), charset);
            String[] arr = s.split("-");
            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != null) {
                        String[] arrValues = arr[i].split(":");
                        if (arrValues != null) {
                            data.put(arrValues[0], arrValues[2]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void changeScannerToValue() {
        addType();
        Map<String, String> variables = getDataFromFile();
        Charset charset = StandardCharsets.UTF_8;
        List<File> studentCodeFiles = new ArrayList<>();
        getAllFiles(STUDENT_PATH, studentCodeFiles, ".java");
        boolean isMethod = false;
        int caseTime = 0;
        if (studentCodeFiles.size() > 0) {
            for (File file : studentCodeFiles) {
                if (file.getName().toLowerCase().contains("cabinet")) {
                    try {
                        String result = "";
                        Path path = Paths.get(file.getAbsolutePath());
                        List<String> content = null;
                        try {
                            content = Files.readAllLines(path);
                            for (int i = 0; i < content.size(); i++) {
                                String line = content.get(i);
//                                for (Map.Entry<String, String> entry : variables.entrySet()) {
//                                    if (isVariable(line.toLowerCase(),entry.getKey()) && line.contains("=") && !line.contains("!=") && !line.contains("==")) {
//                                        String[] tempString = line.split("=");
//                                        if (tempString.length == 2) {
//                                            String firstPart = tempString[0];
//                                            if (firstPart.contains(entry.getKey()) && !firstPart.contains("this.")) {
//                                                boolean flag = false;
//                                                for (Map.Entry<String, String> item : type.entrySet()) {
//                                                    if (entry.getValue().equals(item.getKey())) {
//                                                        tempString[1] = item.getValue() + "DBManager.getValue(\"" + entry.getKey() + "\"));";
//                                                        flag = true;
//                                                    }
//                                                }
//                                                if (!flag) {
//                                                    tempString[1] = "DBManager.getValue(\"" + entry.getKey() + "\");";
//                                                }
//                                            }
//                                            line = String.join("=", tempString);
//                                        }
//
//                                    }
//                                }
                                if (line.contains("//StartList")) {
                                    isMethod = true;
                                }
                                if (isMethod && !line.contains("//") && !line.contains("static")) {
                                    line = "static " + line;
                                    line = line.replace("private", "");
                                }
                                if (line.contains("//EndList")) {
                                    isMethod = false;
                                }
//                                if (line.contains("case")) {
//                                    caseTime += 1;
//                                }
//                                if (line.contains("break")) {
//                                    if (caseTime > 0) {
//                                        caseTime -= 1;
//                                    } else {
//                                        line = "";
//                                    }
//                                }
//                                if (line.contains("do{")) {
//                                    line = line.replace("do", "");
//                                }
//                                if (line.contains("while")) {
//                                    line = removeWhile(line);
//                                }
//                                line = line.replace(" do ", "");
                                result += line + "\n";
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
    }

    private static boolean isVariable(String testStr, String key) {
        String regexSingleVariable = " " + key + " ";
        String regexVariableWithEqual = " " + key + "=";
        if (testStr.contains(regexSingleVariable) || testStr.contains(regexVariableWithEqual)) return true;
        return false;
    }

    private static String removeWhile(String a) {
        String firstPart = a.substring(0, a.indexOf("while"));
        String secondPart = a.substring(a.indexOf("while"));
        String removeStr = "";
        char[] s = secondPart.toCharArray();
        for (char c : s
        ) {
            if (c != '{') {
                removeStr += c;
            } else {
                break;
            }
        }
        return firstPart + secondPart.replace(removeStr, "");
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
