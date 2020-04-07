package com.fpt.submission.dto.request;

import static com.fpt.submission.utils.PathUtils.*;


import java.io.File;
import java.io.Serializable;

public class PathDetails implements Serializable {

    private PracticalInfo practicalInfo;
    private String curPractical;
    private String practicalExamCode;
    private String pathSubmission;
    private String pathServer;
    private String pathServerEvaluatingLogFile;
    private String pathServerLogFile;

    private String pathResultFile;
    private String pathTestScript;
    private String pathDataBaseUtils;
    private String pathDBTools;



    // Java
    private String pathJavaServerSubmit;
    private String pathJavaServerStudent;
    private String pathJavaServerTestFol;
    private String javaExecuteCmd;

    // Java web
    private String pathJavaWebServerSubmit;
    private String pathJavaWebServerSubmitDelete;
    private String pathJavaWebServerWebApp;
    private String pathJavaWebServerWebAppDelete;
    private String pathJavaWebServerTestFol;
    private String javaWebExecuteCmd;
    private String pathJavaWebContextFile;
    private String pathJavaWebDBUtilsFile;
    private String pathJavaWebDBUtilsFileChecked;

    //C
    private String pathCTestFol;
    private String pathCXMLResultFile;
    private String pathCServerSubmit;
    private String pathCServerSubmitDelete;
    private String cExecuteCmd;

    // CSharp
    private String pathCSharpTest;
    private String pathCSharpServerSubmit;
    private String pathCSharpServerSubmitDelete;
    private String cSharpExecuteCmd;


    public PathDetails(PracticalInfo practicalInfo) {
        this.practicalInfo = practicalInfo;
        curPractical = PROJECT_DIR + File.separator
                + "PracticalExams" + File.separator
                + practicalInfo.getName();
        this.pathServer = curPractical + File.separator + "Server";
        this.pathDBTools = curPractical + File.separator + "DBTools";
        this.pathSubmission = curPractical + File.separator + "Submissions";
        this.practicalExamCode = practicalInfo.getExamCode();
        this.pathResultFile = curPractical + File.separator + "Result.txt";
        this.pathDataBaseUtils = curPractical + File.separator + practicalInfo.getName() + ".java";
        this.pathTestScript = curPractical + File.separator + "TestScripts";
        this.pathServerEvaluatingLogFile = curPractical + File.separator + "Server" + File.separator + "evaluating.log";
        this.pathServerLogFile = curPractical + File.separator + "Server" + File.separator + "servertest.log";
    }



    public String getPracticalExamCode() {
        return practicalExamCode;
    }

    public String getPathSubmission() {
        return pathSubmission;
    }

    public String getPathDataBaseUtils() {
        return pathDataBaseUtils;
    }

    public String getPathServer() {
        return pathServer;
    }

    public String getPathDBTools() {
        return pathDBTools;
    }

    public String getPathServerEvaluatingLogFile() {
        return pathServerEvaluatingLogFile;
    }

    public String getPathServerLogFile() {
        return pathServerLogFile;
    }

    public String getPathResultFile() {
        return pathResultFile;
    }

    public String getPathTestScript() {
        return pathTestScript;
    }

    public String getPathJavaServerSubmit() {
        this.pathJavaServerSubmit = pathServer + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "practicalexam" + File.separator;
        return pathJavaServerSubmit;
    }

    public String getPathJavaServerStudent() {
        this.pathJavaServerStudent = pathServer + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "practicalexam" + File.separator
                + "student";
        return pathJavaServerStudent;
    }

    public String getPathJavaServerTestFol() {
        this.pathJavaServerTestFol = pathServer
                + File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "practicalexam" + File.separator;
        return pathJavaServerTestFol;
    }

    public String getJavaExecuteCmd() {
        this.javaExecuteCmd = "cd " + pathServer + "&mvn clean package -l evaluating.log";
        return javaExecuteCmd;
    }

    public String getPathJavaWebServerSubmit() {
        this.pathJavaWebServerSubmit = pathServer + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "practicalexam" + File.separator;
        return pathJavaWebServerSubmit;
    }

    public String getPathJavaWebServerSubmitDelete() {
        this.pathJavaWebServerSubmitDelete = pathServer + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "java" + File.separator
                + "com" + File.separator
                + "practicalexam" + File.separator
                + "student";
        return pathJavaWebServerSubmitDelete;
    }

    public String getPathJavaWebServerWebApp() {
        this.pathJavaWebServerWebApp = pathServer + File.separator
                + "src" + File.separator
                + "main" + File.separator;
        return pathJavaWebServerWebApp;
    }

    public String getPathJavaWebServerWebAppDelete() {
        this.pathJavaWebServerWebAppDelete = pathServer + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "webapp";
        return pathJavaWebServerWebAppDelete;
    }

    public String getPathJavaWebServerTestFol() {
        this.pathJavaWebServerTestFol = pathServer
                + File.separator
                + "src" + File.separator
                + "test" + File.separator
                + "java" + File.separator
                + "server" + File.separator;
        return pathJavaWebServerTestFol;
    }

    public String getPathJavaWebDBUtilsFile() {
        this.pathJavaWebDBUtilsFile = curPractical + File.separator + "Server" +
                File.separator + "src"
                + File.separator + "main"
                + File.separator + "java"
                + File.separator + "com"
                + File.separator + "practicalexam"
                + File.separator + "student"
                + File.separator + "connection"
                + File.separator  + "DBUtilities.java";
        return this.pathJavaWebDBUtilsFile;
    }


    public String getPathJavaWebContextFile() {
        this.pathJavaWebContextFile = curPractical + File.separator + "Server" +
                File.separator + "src"
                + File.separator + "main"
                + File.separator + "webapp"
                + File.separator + "META-INF"
                + File.separator + "context.xml";
        return this.pathJavaWebContextFile;
    }

    public String getJavaWebExecuteCmd() {
        this.javaWebExecuteCmd = "cd " + pathServer + "&mvn clean package -l evaluating.log";
        return javaWebExecuteCmd;
    }

    public String getPathCTestFol() {
        this.pathCTestFol = pathServer
                + File.separator
                + "src";
        return pathCTestFol;
    }

    public String getPathCXMLResultFile() {
        this.pathCXMLResultFile = pathServer
                + File.separator
                + "src" + File.separator +
                "CUnitAutomated-Results.xml";
        return pathCXMLResultFile;
    }

    public String getPathCServerSubmit() {
        this.pathCServerSubmit = pathCTestFol + File.separator;
        return pathCServerSubmit;
    }

    public String getPathCServerSubmitDelete() {
        this.pathCServerSubmitDelete = pathServer + File.separator
                + "src" + File.separator;
        return pathCServerSubmitDelete;
    }

    public String getCExecuteCmd(String scriptCode) {
        String s = "cd " + pathCTestFol + "&" +
                "gcc " + scriptCode + ".c -lcunit -o app&app.exe";
        this.cExecuteCmd = s;
        return cExecuteCmd;
    }

    public String getPathCSharpTest() {
        this.pathCSharpTest = pathServer
                + File.separator
                + "Tests" + File.separator
                + "Controllers" + File.separator;
        return pathCSharpTest;
    }

    public String getPathCSharpServerSubmit() {
        this.pathCSharpServerSubmit = pathServer
                + File.separator
                + "TemplateAutomatedTest" + File.separator;
        return pathCSharpServerSubmit;
    }

    public String getPathCSharpServerSubmitDelete() {
        this.pathCSharpServerSubmitDelete = pathServer
                + File.separator
                + "TemplateAutomatedTest"
                + File.separator
                + "Student";
        return pathCSharpServerSubmitDelete;
    }

    public String getCSharpExecuteCmd() {
        this.cSharpExecuteCmd = "cd " + pathServer + "&dotnet clean&dotnet test";
        return cSharpExecuteCmd;
    }
}
