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
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static void copyAllDBToolsFile(PathDetails pathDetails) {
        List<File> files = new ArrayList<>();
        getAllFiles(pathDetails.getPathDBTools(), files, ".java");
        for (File file : files) {
            try {
                Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(pathDetails.getPathJavaServerStudent() + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeScannerToValue(PathDetails pathDetails, String testDataCode, String extension) {
        List<String> variables = new ArrayList<>();
        // Get list student object variables declared by the lecturer
        Charset charset = StandardCharsets.UTF_8;
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
                        if (arrValues != null && !arrValues[0].equals("")) {
                            variables.add(arrValues[0]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<File> studentCodeFiles = new ArrayList<>();
        getAllFiles(pathDetails.getPathJavaServerStudent(), studentCodeFiles, ".java");
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
