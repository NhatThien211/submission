package com.fpt.submission.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.dto.request.StudentPointDto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileUtils {

    public static void copyAllFiles(String from, String to, String extension) {
        List<File> files = new ArrayList<>();
        getAllFiles(from, files, extension);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                try {
                    if (file.getName().contains("DBUtilities")) {
                        Files.copy(Paths.get(file.getAbsolutePath()),
                                Paths.get(to + File.separator + "DBUtilities" + extension), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        Files.copy(Paths.get(file.getAbsolutePath()),
                                Paths.get(to + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyDBUtilsToDBChecked(String dbUtilsPath, String dbUtilsCheckedPath) {
        File file = new File(dbUtilsPath);
        if (file.exists()) {
            Path path = Paths.get(file.getAbsolutePath());
            Charset charset = StandardCharsets.UTF_8;
            String content = null;
            try {
                content = new String(Files.readAllBytes(path), charset);
                content = content.replaceAll("DBUtilities", "DBUtilitiesChecked");
                Files.write(Paths.get(dbUtilsCheckedPath), content.getBytes(charset));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void changeResourceBundle(String configPath, String extension) {
        List<File> configFiles = new ArrayList<>();
        getAllFiles(configPath, configFiles, extension);
        if (configFiles.size() > 0) {
            for (File file : configFiles) {
                if (file.exists()) {
                    try {
                        String result = "";
                        Path path = Paths.get(file.getAbsolutePath());
                        List<String> content = null;
                        try {
                            content = Files.readAllLines(path);
                            for (int i = 0; i < content.size(); i++) {
                                String line = content.get(i);
                                if (line.contains("ResourceBundle") && line.contains("getBundle")) {
                                    line = "ResourceBundle bundle = ResourceBundle.getBundle(\"config\");";
                                }
                                result += line + "\n";
                            }
                        } catch (IOException e) {
                        }
                        Files.write(path, result.getBytes(StandardCharsets.UTF_8));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static Map<String, String> type = new HashMap<>();

    private static void addType() {
        type.clear();
        type.put("int", "Integer.parseInt(");
        type.put("double", "Double.parseDouble(");
        type.put("float", "Float.parseFloat(");
    }

    private static Map<String, String> getDataFromFile(PathDetails pathDetails, String testDataCode, String extension) {
        Charset charset = StandardCharsets.UTF_8;
        Map<String, String> data = new LinkedHashMap<>();
        try {
            String fileName = testDataCode.replace(extension, "") + ".txt";
            String testDataPath = pathDetails.getPathTestScript() + File.separator + fileName;
            Files.copy(Paths.get(testDataPath), Paths.get(pathDetails.getPathServer() + File.separator + "testdata.txt"), StandardCopyOption.REPLACE_EXISTING);
            String s = new String(Files.readAllBytes(Paths.get(testDataPath)), charset);
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


    public static void changeScannerToValue(PathDetails pathDetails, String testDataCode, String extension) {
        addType();
        Map<String, String> variables = getDataFromFile(pathDetails, testDataCode, extension);
        Charset charset = StandardCharsets.UTF_8;
        List<File> studentCodeFiles = new ArrayList<>();
        getAllFiles(pathDetails.getPathJavaServerStudent(), studentCodeFiles, extension);
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
                                for (Map.Entry<String, String> entry : variables.entrySet()) {
                                    if (line.toLowerCase().contains(entry.getKey().toLowerCase())) {
                                        String[] tempString = line.split("=");
                                        if (tempString.length == 2) {
                                            String firstPart = tempString[0];
                                            if (firstPart.contains(entry.getKey()) && !firstPart.contains("this.")) {
                                                boolean flag = false;
                                                for (Map.Entry<String, String> item : type.entrySet()) {
                                                    if (entry.getValue().equals(item.getKey())) {
                                                        tempString[1] = item.getValue() + "DBManager.getValue(\"" + entry.getKey() + "\"));";
                                                        flag = true;
                                                    }
                                                }
                                                if (!flag) {
                                                    tempString[1] = "DBManager.getValue(\"" + entry.getKey() + "\");";
                                                }
                                            }
                                            line = String.join("=", tempString);
                                        }

                                    }
                                }
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
                                if (line.contains("case")) {
                                    caseTime += 1;
                                }
                                if (line.contains("break")) {
                                    if (caseTime > 0) {
                                        caseTime -= 1;
                                    } else {
                                        line = "";
                                    }
                                }
                                if (line.contains("do{")) {
                                    line = line.replace("do", "");
                                }
                                if (line.contains("while")) {
                                    line = removeWhile(line);
                                }
                                line = line.replace(" do ", "");
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

    public static void convertHtmlFileToJspFileInWebApp(String path) {
        List<File> webAppFiles = new ArrayList<>();
        getAllFiles(path, webAppFiles, ".html");
        for (File file : webAppFiles) {
            Path source = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(path + File.separator + file.getName().replace(".html", "") + ".jsp");
            try {
                Files.copy(source, destination);
            } catch (IOException e) {
                System.out.println("Not replace file");
            }
        }
    }

    public static void changeExtensionHtmlToJspInCode(String studentPath) {
        List<File> studentCodeFiles = new ArrayList<>();
        getAllFiles(studentPath, studentCodeFiles, ".java");
        if (studentCodeFiles.size() > 0) {
            for (File file : studentCodeFiles) {

                try {
                    Path path = Paths.get(file.getAbsolutePath());
                    Charset charset = StandardCharsets.UTF_8;
                    String content = new String(Files.readAllBytes(path), charset);
                    content = content.replaceAll("\\.html\";", ".jsp\";");
                    Files.write(path, content.getBytes(charset));
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

    public static void writeString(String resultPath, StudentPointDto dto) {

        FileWriter writer = null;
        try {
            File file = new File(resultPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String startString = "Start" + dto.getStudentCode();
            String endString = "End" + dto.getStudentCode();
            String str = readFileAsString(resultPath);
            int startIndex = str.indexOf(startString);
            int endIndex = str.indexOf(endString);
            if (startIndex >= 0 && endIndex > 0) {
                String toBeReplaced = str.substring(startIndex, endIndex + endString.length());
                str = str.replace(toBeReplaced, "");
            }
            writer = new FileWriter(resultPath);
            // convert student point object to JSON
            String studentPointJson = objectMapper.writeValueAsString(dto);
            if (writer != null) {
                str += "Start" + dto.getStudentCode() + studentPointJson + "End" + dto.getStudentCode();
                writer.write(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFileAsString(String fileName) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
