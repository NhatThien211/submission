package com.fpt.submission.dto.request;

import static com.fpt.submission.utils.PathUtils.*;


import java.io.File;
import java.io.Serializable;

public class PathDetails implements Serializable {

    private PracticalInfo practicalInfo;
    private String curPractical;

    public PathDetails(PracticalInfo practicalInfo) {
        this.practicalInfo = practicalInfo;
        curPractical = PROJECT_DIR + File.separator
                + "PracticalExams" + File.separator
                + practicalInfo.getName() ;
    }

    public String getPathSubmission() {
        return curPractical + File.separator + "Submissions";
    }

    public String getPathServer() {
        return curPractical + File.separator + "Server";
    }

    public String getPathTestScripts() {
        return curPractical + File.separator + "TestScripts";
    }


    // For Java
    public String getPathJavaFol() {
        return getPathServer() + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator;
    }

    public String getPathTestFol() {
        return getPathServer()
                + File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "fpt" + File.separator
                + "practical" + File.separator
                + "java" + File.separator;
    }

    public String getPathJavaComFol() {
        return getPathJavaFol() + File.separator + "com";
    }

    public String getJavaExecuteCmd() {
        return "cd " + getPathServer() + "&mvn clean package";
    }

    // For C#
}
