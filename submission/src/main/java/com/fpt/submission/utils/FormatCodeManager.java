package com.fpt.submission.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.fpt.submission.utils.FileUtils.getAllFiles;

public class FormatCodeManager {

    public static void cleanCodeC(String configPath, String ignoreFilePath, String extension) {
        List<File> configFiles = new ArrayList<>();
        getAllFiles(configPath, configFiles, extension);
        if (configFiles.size() > 0) {
            String ignoreLineArr = FileUtils.readFileAsString(ignoreFilePath);
            for (File file : configFiles) {
                try {
                    String result = "";
                    Path path = Paths.get(file.getAbsolutePath());
                    List<String> content = null;
                    try {
                        content = Files.readAllLines(path);
                        for (int i = 0; i < content.size(); i++) {
                            String line = content.get(i);
                            boolean check = false;
                            String[] arr = ignoreLineArr.split("-");
                            for (int j = 0; j < arr.length; j++) {
                                if (line.contains(arr[j])) {
                                    check = true;
                                }
                            }
                            if (!check) {
                                result += line + "\n";
                            }
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
