package com.practicalexam;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.Serializable;
import java.util.Map;

public class TestResultLoggerExtension implements TestWatcher, AfterAllCallback {

    private static String PROJECT_DIR = System.getProperty("user.dir");
    private static String PATH_JAVA_FOLDER_TEST = PROJECT_DIR + File.separator
            + "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "com" + File.separator
            + "practicalexam";

    private static final String PREFIX_METHOD = "()";
    private static final String PREFIX_TEST = "EXAM_";
    private static final String TXT_RESULT_NAME = "Result.txt";

    private static Map<String, Double> testResultsStatus = new HashMap<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

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
                            testResultsStatus.put(questionName + "-" + arr[1], point);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static StudentPointDto getStudentPointGenerated() {
        StudentPointDto dto = new StudentPointDto();
        String studentCode = getStudentCode();
        String path = PROJECT_DIR.replace("\\Server", "") + File.separator + TXT_RESULT_NAME;
        String startString = "Start" + studentCode;
        String endString = "End" + studentCode;
        String str = readFileAsString(path);
        int startIndex = str.indexOf(startString);
        int endIndex = str.indexOf(endString);
        if (startIndex >= 0 && endIndex > 0) {
            String json = str.substring(startIndex + startString.length(), endIndex);
            try {
                return objectMapper.readValue(json, StudentPointDto.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
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
    public void afterAll(ExtensionContext context) {
        StudentPointDto studentPointDto = getStudentPointGenerated();
        FileWriter writer = null;
        try {
            studentPointDto = setResult(studentPointDto);
        } catch (Exception e) {
            if (studentPointDto == null) {
                studentPointDto = new StudentPointDto();
            }
            studentPointDto.setStudentCode(getStudentCode());
            studentPointDto.setErrorMsg(e.getMessage() + "");
        } finally {
            try {
                String resultPath = PROJECT_DIR.replace("\\Server", "") + File.separator + TXT_RESULT_NAME;
                String startString = "Start" + studentPointDto.getStudentCode();
                String endString = "End" + studentPointDto.getStudentCode();
                String str = readFileAsString(resultPath);
                int startIndex = str.indexOf(startString);
                int endIndex = str.indexOf(endString);
                if (startIndex >= 0 && endIndex > 0) {
                    String toBeReplaced = str.substring(startIndex, endIndex + endString.length());
                    str = str.replace(toBeReplaced, "");
                }
                writer = new FileWriter(resultPath);
                // convert student point object to JSON
                String studentPointJson = objectMapper.writeValueAsString(studentPointDto);
                System.out.println(studentPointJson);
                if (writer != null) {
                    str += "Start" + studentPointDto.getStudentCode() + studentPointJson + "End" + studentPointDto.getStudentCode();
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

    public StudentPointDto setResult(StudentPointDto studentPointDto) {
        // TODO: For re-submit
        Map<String, String> listQuestions = new HashMap<>();
        double totalPoint = 0;
        Integer correctQuestionCount = 0;
        String studentCode = getStudentCode();
        try {
            for (Map.Entry<String, Double> entry : testResultsStatus.entrySet()) {
                System.out.println(entry.getKey());
                String[] temp = entry.getKey().split("-");
                String questionName = temp[0];
                String point = temp[1];
                Double pointCorrect = entry.getValue();
                System.out.println("Result question: " + questionName + ": " + point);
                if (entry.getValue() > 0.0) {
                    totalPoint += entry.getValue();
                    correctQuestionCount++;
                    listQuestions.put(questionName + ":Success", pointCorrect + "/" + point);
                } else {
                    listQuestions.put(entry.getKey() + ":Failed", "0/" + point);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Send TCP messages to Lec-app after finish evaluate
        studentPointDto.setStudentCode(studentCode);
        studentPointDto.setListQuestions(listQuestions);
        studentPointDto.setTotalPoint(String.valueOf(totalPoint));
        studentPointDto.setEvaluateTime(getCurTime());
        studentPointDto.setResult(correctQuestionCount + "/" + testResultsStatus.size());
        return studentPointDto;
    }

    public String getCurTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String getStudentCode() {
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
//        return "SEDUMMY";
        return "";
    }

    static class StudentPointDto implements Serializable {

        private String studentCode;
        private Map<String, String> listQuestions;
        private String totalPoint;
        private String submitTime;
        private String evaluateTime;
        private Double codingConvention;
        private String result;
        private String errorMsg;

        public StudentPointDto() {
        }

        public StudentPointDto(String studentCode, Map<String, String> listQuestions, String totalPoint, String submitTime, String evaluateTime, Double codingConvention, String result, String errorMsg) {
            this.studentCode = studentCode;
            this.listQuestions = listQuestions;
            this.totalPoint = totalPoint;
            this.submitTime = submitTime;
            this.evaluateTime = evaluateTime;
            this.codingConvention = codingConvention;
            this.result = result;
            this.errorMsg = errorMsg;
        }

        public String getStudentCode() {
            return studentCode;
        }

        public void setStudentCode(String studentCode) {
            this.studentCode = studentCode;
        }

        public Map<String, String> getListQuestions() {
            return listQuestions;
        }

        public void setListQuestions(Map<String, String> listQuestions) {
            this.listQuestions = listQuestions;
        }

        public String getTotalPoint() {
            return totalPoint;
        }

        public void setTotalPoint(String totalPoint) {
            this.totalPoint = totalPoint;
        }

        public String getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(String submitTime) {
            this.submitTime = submitTime;
        }

        public String getEvaluateTime() {
            return evaluateTime;
        }

        public void setEvaluateTime(String evaluateTime) {
            this.evaluateTime = evaluateTime;
        }

        public Double getCodingConvention() {
            return codingConvention;
        }

        public void setCodingConvention(Double codingConvention) {
            this.codingConvention = codingConvention;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}