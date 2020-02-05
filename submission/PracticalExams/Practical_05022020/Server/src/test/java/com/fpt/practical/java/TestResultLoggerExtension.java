package com.fpt.practical.java;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class TestResultLoggerExtension implements TestWatcher, AfterAllCallback {

    private String PROJECT_DIR = System.getProperty("user.dir");
    private String PATH_JAVA_FOLDER_TEST = PROJECT_DIR + File.separator
            + "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "fpt" + File.separator
            + "practical" + File.separator
            + "java";

    private final String PREFIX_METHOD = "()";
    private final String PREFIX_TEST = "EXAM_";
    private final String TXT_RESULT_NAME = "Result.txt";

    private Map<String, Double> testResultsStatus = new HashMap<>();

    public void checkQuestionPoint(String nameQuestionCheck, boolean isCorrect) {
        String questionPointStr = JavaApplicationTests.questionPointStr;
        Double point = 0.0;
        if (questionPointStr != null && !questionPointStr.equals("")) {
            String[] questionArr = questionPointStr.split("-");
            if (questionArr != null) {
                for (int i = 0; i < questionArr.length; i++) {
                    String[] arr = questionArr[i].split(":");
                    if (arr != null) {
                        String questionName = arr[0] + PREFIX_METHOD;
                        if (nameQuestionCheck.equalsIgnoreCase(questionName)) {
                            if (isCorrect) {
                                point = Double.parseDouble(arr[1]);
                            }
                            testResultsStatus.put(questionName, point);
                            break;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        System.out.println("Test Disabled for test: with reason :- " +
                context.getDisplayName() +
                reason.orElse("No reason"));
        checkQuestionPoint(context.getDisplayName(), false);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("Test Successful for test: " + context.getDisplayName());
        checkQuestionPoint(context.getDisplayName(), true);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println("Test Aborted for test: " + context.getDisplayName());
        checkQuestionPoint(context.getDisplayName(), false);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("Test Aborted for test: " + context.getDisplayName());
        checkQuestionPoint(context.getDisplayName(), false);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        appendStringToResultFile();
    }

    public void appendStringToResultFile() {
        // TODO: For re-submit
        String resultPath = PROJECT_DIR.replace("\\Server", "") + File.separator + TXT_RESULT_NAME;
        File file = null;
        PrintWriter writer = null;
        try {
            file = new File(resultPath);
            writer = new PrintWriter(new FileWriter(file, true));
            double totalPoint = 0;
            Integer correctQuestionCount = 0;
            String resultText = "";
            resultText += getStudentCode() + "\n";
            for (Map.Entry<String, Double> entry : testResultsStatus.entrySet()) {
                if (entry.getValue() > 0.0) {
                    resultText += entry.getKey() + ": Passed \n";
                    totalPoint += entry.getValue();
                    correctQuestionCount++;
                } else {
                    resultText += entry.getKey() + ": Failed \n";
                }
            }
            resultText += "Time : " + getCurTime() + "\n";
            resultText += "Result : " + correctQuestionCount +" / "+ testResultsStatus.size() + "\n";
            resultText += "Total : " + totalPoint + "\n";
            resultText += "end" + getStudentCode() + "\n";

            writer.println(resultText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    public static String getCurTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public String getStudentCode() {
        String path = PATH_JAVA_FOLDER_TEST;
        System.out.println(path);
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String s = file.getName();
                if (s.contains(PREFIX_TEST)) {
                    String[] arr = s.split("_");
                    return arr[1];
                }
            }
        }
        return "";
    }
}