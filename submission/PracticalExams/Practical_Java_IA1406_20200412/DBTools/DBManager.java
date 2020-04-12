package com.practicalexam.student;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBManager {

    public static String getValue(String key) {
        Charset charset = StandardCharsets.UTF_8;
        String replaceValue = "";
        try {
            String s = new String(Files.readAllBytes(Paths.get("testdata.txt")), charset);
            String[] arr = s.split("-");
            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != null) {
                        String[] arrValues = arr[i].split(":");
                        if (arrValues != null) {
                            if (key.equals(arrValues[0])) {
                                return arrValues[1];
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return replaceValue;
    }
}
