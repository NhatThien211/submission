package com.fpt.submission.service.serviceImpl;

import static com.fpt.submission.constants.CommonConstant.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fpt.submission.constants.CommonConstant;
import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.dto.request.StudentPointDto;
import com.fpt.submission.exception.CustomException;
import com.fpt.submission.utils.*;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;


@EnableAsync
@Service
public class EvaluationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationManager.class);
    private Boolean isEvaluating;
    private Queue<StudentSubmitDetail> submissionQueue;
    private List<String> examScriptsList;
    private String PREFIX_EXAM_SCRIPT = "EXAM_";
    private PathDetails pathDetails;
    private static final String START_POINT_ARR = "START_POINT_ARR";
    private static final String END_POINT_ARR = "END_POINT_ARR";
    private static final String COMPILE_ERROR = "COMPILATION ERROR";
    private static final String NOT_FOUND_STUDENT = "NOT_FOUND_STUDENT";
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final int MAX_TIME = 10;

    @Autowired
    public EvaluationManager() {
        isEvaluating = false;
        submissionQueue = new PriorityQueue<>();
        pathDetails = PathUtils.pathDetails;
        examScriptsList = getExamScriptsList();
    }


    // Get all exams code in TestScript folder
    private List<String> getExamScriptsList() {
        PathDetails pathDetails = PathUtils.pathDetails;
        List<String> result = null;
        if (pathDetails != null) {
            try {
                result = new ArrayList<>();
                String s = pathDetails.getPathTestScript() + File.separator;
                File folder = new File(s);
                if (folder != null) {
                    for (final File file : folder.listFiles()) {
                        if (file.isFile()) {
                            result.add(file.getName());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Async("TaskExecutor")
    @EventListener
    public void evaluate(StudentSubmitDetail submissionEvent) {
        System.out.println(Thread.currentThread().getName() + "-" + submissionEvent.getStudentCode() + ":" + isEvaluating);

        StudentPointDto generatedResult = new StudentPointDto();
        generatedResult.setTotalPoint("0");
        generatedResult.setStudentCode(submissionEvent.getStudentCode());
        FileUtils.writeString(pathDetails.getPathResultFile(), generatedResult);

        submissionQueue.add(submissionEvent);
        if (!isEvaluating && submissionQueue.size() > 0) {
            isEvaluating = true;
            System.out.println(submissionEvent.getStudentCode() + "-" + pathDetails.getPracticalExamCode());
            // Generate result

            String examCode = pathDetails.getPracticalExamCode();
            if (examCode.contains(CODE_PRACTICAL_JAVA_WEB)) {
                evaluateSubmissionJavaWeb(submissionQueue.remove());
            } else if (examCode.contains(CODE_PRACTICAL_JAVA)) {
                evaluateSubmissionJava(submissionQueue.remove());
            } else if (examCode.contains(CODE_PRACTICAL_CSharp)) {
                evaluateSubmissionCSharp(submissionQueue.remove());
            } else if (examCode.contains(CODE_PRACTICAL_C)) {
                evaluateSubmissionC(submissionQueue.remove());
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND, "Not found Path Details Exam code");
            }
        } else {
            LOGGER.info("[EVALUATE] Waiting - : " + submissionEvent.getStudentCode());
        }
    }


    private void evaluateSubmissionC(StudentSubmitDetail dto) {
        File serverTestScriptPath = null;
        try {
            LOGGER.info("[EVALUATE] Student code : " + dto.getStudentCode());
            StudentPointDto result = new StudentPointDto();
            result.setStudentCode(dto.getStudentCode());
            String scriptCode = "";
            if (!dto.getScriptCode().equals("")) {
                scriptCode = dto.getScriptCode().substring(0, dto.getScriptCode().lastIndexOf("_"));
            }
            File sourceScriptPath = new File(pathDetails.getPathTestScript() + File.separator + scriptCode + EXTENSION_C);
            //copy source to target using Files Class
            if (!sourceScriptPath.exists()) {
                System.out.println("[PATH-SCRIPT-NOT FOUND]" + dto.getStudentCode() + "-" + dto.getScriptCode());
                result.setErrorMsg("Not found script " + dto.getScriptCode());
                // Error
            } else {

                File file = new File(pathDetails.getPathCTestFol());
                if (!file.exists()) {
                    file.mkdir();
                }

                ZipFile.unzip(pathDetails.getPathSubmission() + File.separator + dto.getStudentCode() + EXTENSION_ZIP,
                        file.getAbsolutePath());
                // Pre evaluating
                serverTestScriptPath = new File(file.getAbsolutePath() + File.separator + PREFIX_EXAM_SCRIPT
                        + dto.getStudentCode() + "_" + scriptCode + EXTENSION_C);

                Files.copy(sourceScriptPath.toPath(), serverTestScriptPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // Chạy CMD file test
                String cmd = pathDetails.getCExecuteCmd(PREFIX_EXAM_SCRIPT + dto.getStudentCode() + "_" + scriptCode);
                CmdExecution.execute(cmd);


                String questionPointArrStr = "";
                List<String> content = Files.readAllLines(serverTestScriptPath.toPath());
                for (String line : content) {
                    if (line.contains(START_POINT_ARR) && line.contains(END_POINT_ARR)) {
                        questionPointArrStr = line.replace(START_POINT_ARR, "")
                                .replace(END_POINT_ARR, "").trim();
                        break;
                    }
                }

                String jsonStr = generateCTestResult(questionPointArrStr, dto.getStudentCode());
                System.out.println(jsonStr);
                int count = 0;
                sendTCPMessage(jsonStr, count);
                System.out.println("Trả response cho giảng viên");

                if (submissionQueue.size() > 0) {
                    deleteAllFile(dto.getStudentCode(), pathDetails.getPathCServerSubmitDelete(), serverTestScriptPath);
                    evaluateSubmissionC(submissionQueue.remove());
                } else {
                    isEvaluating = false;
                }
                // Trả status đã chấm xong về app lec winform (mssv)

            }
        } catch (Exception e) {
            LOGGER.info( "[EVALUATE-ERROR] Student code : " + dto.getStudentCode());
            e.printStackTrace();
        } finally {
            deleteAllFile(dto.getStudentCode(), pathDetails.getPathCServerSubmitDelete(), serverTestScriptPath);
            isEvaluating = false;
        }
    }

    private String generateCTestResult(String questionPointArrStr, String studentCode) {
        StudentPointDto studentPointDto = new StudentPointDto();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Get evaluated submission result
            File xml = new File(pathDetails.getPathCXMLResultFile());
            if (xml.isFile()) {
                String[] questions = questionPointArrStr.split("-");
                if (questions == null) {
                    throw new CustomException(HttpStatus.NOT_FOUND, "Not found Question point array");
                }
                // Get question point array
                Map<String, Double> questionPointMap = new HashMap<>();

                for (int i = 0; i < questions.length; i++) {
                    String[] arr = questions[i].split(":");
                    String questionName = arr[0];
                    Double point = Double.valueOf(arr[1]);
                    questionPointMap.put(questionName, point);
                }

                ObjectWriter w = new ObjectMapper().writerWithDefaultPrettyPrinter();
                Object o = null;
                o = new XmlMapper()
                        .registerModule(new SimpleModule().addDeserializer(Object.class, new JsonUtils()))
                        .readValue(xml, Object.class);


                JsonNode rootNode = mapper.readTree(w.writeValueAsString(o));
                if (rootNode != null) {
                    List<JsonNode> nodeSuiteSuccessList = rootNode.findValues("CUNIT_RUN_TEST_SUCCESS");
                    List<JsonNode> nodeSuiteFailedList = rootNode.findValues("CUNIT_RUN_TEST_FAILURE");

                    Map<String, String> listQuestions = new HashMap<>();
                    double totalPoint = 0;
                    Integer correctQuestionCount = 0;
                    String time = TimeUtils.getCurTime();
                    if (nodeSuiteSuccessList != null && nodeSuiteSuccessList.size() > 0) {
                        for (JsonNode node : nodeSuiteSuccessList) {
                            JsonNode testNameNode = node.findValue("TEST_NAME");
                            if (testNameNode != null) {
                                String testName = testNameNode.textValue();
                                for (Map.Entry<String, Double> entry : questionPointMap.entrySet()) {
                                    if (testName.trim().equalsIgnoreCase(entry.getKey().trim())) {
                                        String key = entry.getKey();
                                        Double value = entry.getValue();
                                        totalPoint += value;
                                        correctQuestionCount++;
                                        listQuestions.put(key, value + "/" + value);
                                    }
                                }
                            }
                        }
                    }
                    if (nodeSuiteFailedList != null && nodeSuiteFailedList.size() > 0) {
                        for (JsonNode node : nodeSuiteFailedList) {
                            JsonNode testNameNode = node.findValue("TEST_NAME");
                            if (testNameNode != null) {
                                String testName = testNameNode.textValue();
                                for (Map.Entry<String, Double> entry : questionPointMap.entrySet()) {
                                    if (testName.equalsIgnoreCase(entry.getKey())) {
                                        String key = entry.getKey();
                                        Double value = entry.getValue();
                                        listQuestions.put(key, "0/" + value);
                                    }
                                }
                            }
                        }
                    }

                    studentPointDto.setStudentCode(studentCode);
                    studentPointDto.setListQuestions(listQuestions);
                    studentPointDto.setTotalPoint(String.valueOf(totalPoint));
                    studentPointDto.setEvaluateTime(time);
                    studentPointDto.setResult(correctQuestionCount + "/" + questionPointMap.size());
                }
            }
        } catch (IOException e) {
            studentPointDto.setErrorMsg(e.getMessage() + "");
            LOGGER.info( "[EVALUATE] Student code : " + studentCode);
            e.printStackTrace();
        }

        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(studentPointDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    private void evaluateSubmissionJava(StudentSubmitDetail dto) {
        File serverTestScriptPath = null;

        try {
            LOGGER.info( "[EVALUATE] Student code : " + dto.getStudentCode());
            // Init result
            StudentPointDto result = new StudentPointDto();
            result.setStudentCode(dto.getStudentCode());
            result.setTotalPoint("0");

            String scriptCode = "";
            if (!dto.getScriptCode().equals("")) {
                scriptCode = dto.getScriptCode().substring(0, dto.getScriptCode().lastIndexOf("_"));
            }
            File sourceScriptPath = new File(pathDetails.getPathTestScript() + File.separator + scriptCode + EXTENSION_JAVA);
            //copy source to target using Files Class
            if (!sourceScriptPath.exists()) {
                System.out.println("[PATH-SCRIPT-NOT FOUND]" + dto.getStudentCode() + "-" + dto.getScriptCode());
                result.setErrorMsg("Not found script " + dto.getScriptCode());
                // Error
            } else {
                // Pre evaluating
                serverTestScriptPath = new File(pathDetails.getPathJavaServerTestFol() +
                        File.separator + PREFIX_EXAM_SCRIPT + dto.getStudentCode() + "_" + scriptCode + EXTENSION_JAVA);

                Files.copy(sourceScriptPath.toPath(), serverTestScriptPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                ZipFile.unzip(pathDetails.getPathSubmission()
                        + File.separator
                        + dto.getStudentCode()
                        + EXTENSION_ZIP, pathDetails.getPathJavaServerSubmit());

                // Copy all DB tools existed
                File testDataFile = new File(pathDetails.getPathDBTools() + File.separator + scriptCode + EXTENSION_TXT);
                if (testDataFile.exists()) {
                    Files.copy(testDataFile.toPath(), Paths.get(pathDetails.getPathFileData()), StandardCopyOption.REPLACE_EXISTING);
                }
                // Copy all config files existed

                // Change Scanner value if exist

//                FileUtils.changeScannerToValue(pathDetails, scriptTextCode, EXTENSION_JAVA);


                // Chạy CMD file test
                CmdExecution.execute(pathDetails.getJavaExecuteCmd());


                String resultText = "";

                if (checkCompileIsError(pathDetails.getPathServerEvaluatingLogFile())) {
                    result.setErrorMsg("Compile Error");
                    result.setTotalPoint("0");
                    result.setEvaluateTime(TimeUtils.getCurTime());
                    resultText = objectMapper.writeValueAsString(result);
                } else {
                    resultText = getTextResult(result);
                    if (resultText.equals("Error")) {
                        result.setErrorMsg("Error");
                        result.setTotalPoint("0");
                    }
                    if (resultText.equals(NOT_FOUND_STUDENT)) {
                        result.setErrorMsg("Submit required");
                        result.setTotalPoint("0");
                        resultText = objectMapper.writeValueAsString(result);
                    }
                }
                System.out.println(resultText);
                int count = 0;
                sendTCPMessage(resultText, count);

                // Send submission to server for check duplicated code and evaluate online
                ZipFile.zipProject(pathDetails.getPathServer(), ZIP_PATH + File.separator + dto.getStudentCode(), EXTENSION_JAVA);
                File file = new File(ZIP_PATH + File.separator + dto.getStudentCode() + EXTENSION_ZIP);
                if (file.exists()) {
                    RequestUtils.sendFile(dto.getStudentCode(), result.getSubmitTime(), result.getTotalPoint(), file, pathDetails.getPracticalExamName());

                }

                if (submissionQueue.size() > 0) {
                    deleteAllFile(dto.getStudentCode(), pathDetails.getPathJavaServerStudent(), serverTestScriptPath);
                    evaluateSubmissionJava(submissionQueue.remove());
                } else {
                    isEvaluating = false;
                }
                // Trả status đã chấm xong về app lec winform (mssv)
                System.out.println("Trả response cho giảng viên");
            }
        } catch (Exception e) {
            LOGGER.info( "[EVALUATE-ERROR] Student code : " + dto.getStudentCode());
            e.printStackTrace();
        } finally {
            deleteAllFile(dto.getStudentCode(), pathDetails.getPathJavaServerStudent(), serverTestScriptPath);
            isEvaluating = false;
        }
    }

    private void evaluateSubmissionJavaWeb(StudentSubmitDetail dto) {
        File serverTestScriptPath = null;
        try {
            LOGGER.info( "[EVALUATE] Student code : " + dto.getStudentCode());
            // Init result
            StudentPointDto result = new StudentPointDto();
            result.setStudentCode(dto.getStudentCode());
            result.setTotalPoint("0");
            String scriptCode = "";
            if (!dto.getScriptCode().equals("")) {
                scriptCode = dto.getScriptCode().substring(0, dto.getScriptCode().lastIndexOf("_"));
            }
            File sourceScriptPath = new File(pathDetails.getPathTestScript() + File.separator + scriptCode + EXTENSION_JAVA);

            //copy source to target using Files Class
            if (!sourceScriptPath.exists()) {
                result.setErrorMsg("Not found script " + dto.getScriptCode());
                // Error
            } else {
                // Pre evaluating
                serverTestScriptPath = new File(pathDetails.getPathJavaWebServerTestFol() + PREFIX_EXAM_SCRIPT + dto.getStudentCode() + "_" + scriptCode + EXTENSION_JAVA);

                Files.copy(sourceScriptPath.toPath(), serverTestScriptPath.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Unzip submission code
                ZipFile.unzip(pathDetails.getPathSubmission()
                                + File.separator
                                + dto.getStudentCode() + ".zip",
                        pathDetails.getPathJavaWebServerSubmit());

                // Unzip webapp file
                ZipFile.unzip(pathDetails.getPathSubmission()
                                + File.separator
                                + dto.getStudentCode() + "_WEB.zip",
                        pathDetails.getPathJavaWebServerWebApp());

                // Set up change extension html to jsp
                FileUtils.convertHtmlFileToJspFileInWebApp(pathDetails.getPathJavaWebServerWebAppDelete());
                FileUtils.changeExtensionHtmlToJspInCode(pathDetails.getPathJavaWebServerSubmitDelete());

                // Duplicated DBUtils to DBUtilsChecked if exist
                FileUtils.copyDBUtilsToDBChecked(pathDetails.getPathDBUtilities(), pathDetails.getPathDBUtilitiesChecked());

                // Copy all DB tools existed

                File testDataFile = new File(pathDetails.getPathDBTools() + File.separator + "DBUtilities_" + scriptCode + EXTENSION_JAVA);
                if (testDataFile.exists()) {
                    Files.copy(testDataFile.toPath(), Paths.get(pathDetails.getPathJavaWebConnectionFol() + File.separator + "DBUtilities.java"), StandardCopyOption.REPLACE_EXISTING);
                }
//                FileUtils.copyAllFiles(pathDetails.getPathDBTools(), pathDetails.getPathJavaWebConnectionFol(), EXTENSION_JAVA);

                // Modify and copy all student config files to resources if existed
                // Change Scanner value if exist
                FileUtils.changeResourceBundle(pathDetails.getPathConfig(), EXTENSION_JAVA);
                FileUtils.copyAllFiles(pathDetails.getPathConfig(), pathDetails.getPathResources(), EXTENSION_CONFIG);

                //
                String resultText = "";
                result.setStudentCode(dto.getStudentCode());


                // Copy make connection file if existed
//            copyConnectionFile();

//             Chạy CMD file test
                CmdExecution.execute(pathDetails.getJavaWebExecuteCmd());
//             Check compile error
                if (checkCompileIsError(pathDetails.getPathServerEvaluatingLogFile())) {
                    result.setErrorMsg("Compile Error");
                    result.setTotalPoint("0");
                    result.setEvaluateTime(TimeUtils.getCurTime());
                    resultText = objectMapper.writeValueAsString(result);
                } else {
                    resultText = getTextResult(result);
                    if (resultText.equals(NOT_FOUND_STUDENT)) {
                        result.setErrorMsg("Submit required");
                        result.setTotalPoint("0");
                        resultText = objectMapper.writeValueAsString(result);
                    }
                }
                int count = 0;
                System.out.println(resultText);
//                sendTCPMessage(resultText, count);

                // Send submission to server for check duplicated code and evaluate online
                ZipFile.zipProject(pathDetails.getPathServer(), ZIP_PATH + File.separator + dto.getStudentCode(), EXTENSION_JAVA);
                File file = new File(ZIP_PATH + File.separator + dto.getStudentCode() + EXTENSION_ZIP);
                if (file.exists()) {
                    RequestUtils.sendFile(dto.getStudentCode(), result.getSubmitTime(), result.getTotalPoint(), file, pathDetails.getPracticalExamName());
                }

                if (submissionQueue.size() > 0) {
                    deleteAllFile(dto.getStudentCode(), pathDetails.getPathJavaWebServerSubmitDelete(), serverTestScriptPath);
                    evaluateSubmissionJavaWeb(submissionQueue.remove());
                } else {
                    isEvaluating = false;
                }
                // Trả status đã chấm xong về app lec winform (mssv)
                System.out.println("Trả response cho giảng viên");
            }
        } catch (Exception e) {

            LOGGER.info( "[EVALUATE-ERROR] Student code : " + dto.getStudentCode());
            e.printStackTrace();
        } finally {
            deleteAllFile(dto.getStudentCode(), pathDetails.getPathJavaServerStudent(), serverTestScriptPath);
            isEvaluating = false;
        }
    }


    private void evaluateSubmissionCSharp(StudentSubmitDetail dto) {

        File serverTestScriptPath = null;
        try {
            LOGGER.info( "[EVALUATE] Student code : " + dto.getStudentCode());
            StudentPointDto result = new StudentPointDto();
            result.setStudentCode(dto.getStudentCode());
            result.setTotalPoint("0");

            String scriptCode = "";
            if (!dto.getScriptCode().equals("")) {
                scriptCode = dto.getScriptCode().substring(0, dto.getScriptCode().lastIndexOf("_"));
            }
            File sourceScriptPath = new File(pathDetails.getPathTestScript() + File.separator + scriptCode + EXTENSION_CSHARP);
            //copy source to target using Files Class
            if (!sourceScriptPath.exists()) {
                System.out.println("[PATH-SCRIPT-NOT FOUND]" + dto.getStudentCode() + "-" + dto.getScriptCode());
                result.setErrorMsg("Not found script " + dto.getScriptCode());
                // Error
            } else {
                // Pre evaluating
                serverTestScriptPath = new File(pathDetails.getPathCSharpTest() + PREFIX_EXAM_SCRIPT +
                        dto.getStudentCode() + "_" + scriptCode + EXTENSION_CSHARP);

                Files.copy(sourceScriptPath.toPath(), serverTestScriptPath.toPath(), StandardCopyOption.REPLACE_EXISTING);

                ZipFile.unzip(pathDetails.getPathSubmission() + File.separator + dto.getStudentCode() + ".zip",
                        pathDetails.getPathCSharpServerSubmit());

                // Copy all DB tools existed
                File connectionFile = new File(pathDetails.getPathDBTools() + File.separator + scriptCode + EXTENSION_CSHARP);
                if (connectionFile.exists()) {
                    Files.copy(connectionFile.toPath(), Paths.get(pathDetails.getPathCSharpDBUtil()), StandardCopyOption.REPLACE_EXISTING);
                }

                // Chạy CMD file test
                CmdExecution.execute(pathDetails.getCSharpExecuteCmd());

                String resultText = "";
                if (checkCompileIsError(pathDetails.getPathServerEvaluatingLogFile())) {
                    result.setErrorMsg("Compile Error");
                    result.setTotalPoint("0");
                    result.setEvaluateTime(TimeUtils.getCurTime());
                    resultText = objectMapper.writeValueAsString(result);
                } else {
                    resultText = getTextResult(result);
                    if (resultText.equals(NOT_FOUND_STUDENT)) {
                        result.setErrorMsg("Submit required");
                        result.setTotalPoint("0");
                        resultText = objectMapper.writeValueAsString(result);
                    }
                }
                int count = 0;
                System.out.println(resultText);
                sendTCPMessage(resultText, count);

                // Send submission to server for check duplicated code and evaluate online
                ZipFile.zipProject(pathDetails.getPathServer(), ZIP_PATH + File.separator + dto.getStudentCode(), EXTENSION_CSHARP);
                File file = new File(ZIP_PATH + File.separator + dto.getStudentCode() + EXTENSION_ZIP);
                if (file.exists()) {
                    RequestUtils.sendFile(dto.getStudentCode(), result.getSubmitTime(), result.getTotalPoint(), file, pathDetails.getPracticalExamName());

                }

                if (submissionQueue.size() > 0) {
                    deleteAllFile(dto.getStudentCode(), pathDetails.getPathCSharpServerSubmitDelete(), serverTestScriptPath);
                    evaluateSubmissionCSharp(submissionQueue.remove());
                } else {
                    isEvaluating = false;
                }
                // Trả status đã chấm xong về app lec winform (mssv)
                System.out.println("Trả response cho giảng viên");
            }
        } catch (Exception e) {
            LOGGER.info( "[EVALUATE-ERROR] Student code : " + dto.getStudentCode());

            e.printStackTrace();
        } finally {
            deleteAllFile(dto.getStudentCode(), pathDetails.getPathCSharpServerSubmitDelete(), serverTestScriptPath);
        }
    }


    private void deleteAllFile(String studentCode, String pathSubmit, File scriptFile) {
        appendLogServerTest(studentCode);
        File file = new File(pathSubmit);
        if (file != null && SubmissionUtils.deleteFolder(file)) {
            LOGGER.info("[DELETE SUBMISSION - SERVER] - " + studentCode);
        }

        if (scriptFile != null && scriptFile.delete()) {
            LOGGER.info("[DELETE SCRIPT - SERVER] - " + studentCode);
        }

        if (pathDetails.getPracticalExamCode().contains(CODE_PRACTICAL_JAVA_WEB)) {
            File webappFol = new File(pathDetails.getPathJavaWebServerWebAppDelete());
            if (webappFol != null && SubmissionUtils.deleteFolder(webappFol)) {
                LOGGER.info("[DELETE SCRIPT - SERVER] - " + studentCode);
            }
        }
    }

    @Bean(name = "TaskExecutor")
    @Primary
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setBeanName("TaskExecutor");
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("[THREAD-EVALUATE]-");
        executor.initialize();
        return executor;
    }

    private Boolean checkCompileIsError(String outputLogPath) {
        List<String> content = null;
        try {
            content = Files.readAllLines(Paths.get(outputLogPath));
            for (String line : content) {
                if (line.contains(COMPILE_ERROR)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }


    private void sendTCPMessage(String result, int count) {
        if (count < MAX_TIME) {
            try {
                count++;
                Thread.sleep(1000);
                SubmissionUtils.sendTCPMessage(result, CommonConstant.SOCKET_SERVER_LOCAL_HOST, CommonConstant.SOCKET_SERVER_LISTENING_PORT);
            } catch (Exception e) {
                e.printStackTrace();
                sendTCPMessage(result, count);
            }
        }
    }

    private String getTextResult(StudentPointDto dto) {
        String path = pathDetails.getPathResultFile();
        String startString = "Start" + dto.getStudentCode();
        String endString = "End" + dto.getStudentCode();
        String str = readFileAsString(path, dto.getStudentCode());
        int startIndex = str.indexOf(startString);
        int endIndex = str.indexOf(endString);
        if (startIndex >= 0 && endIndex > 0) {
            return str.substring(startIndex + startString.length(), endIndex);
        }
        return "Error";
    }

    private String readFileAsString(String fileName, String studentCode) {
        String text = "";
        try {
            text = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info( "[EVALUATE - FILE ERROR] File : " + studentCode + "\n" + e.getMessage());
        }
        return text;
    }

    private void appendLogServerTest(String studentCode) {
        if (pathDetails.getPracticalExamName().contains(CODE_PRACTICAL_C)) {
            return;
        }
        File file = new File(pathDetails.getPathServerEvaluatingLogFile());
        if (!file.exists()) {
            return;
        }
        try {
            String s = "--------------------------------------------------------";
            s += "\n" + studentCode + "\n";
            s += "--------------------------------------------------------\n";
            s += readFileAsString(file.getAbsolutePath(), studentCode);
            String logFile = pathDetails.getPathServerLogFile();
            Files.write(Paths.get(logFile), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.error("Not found servertest.log");
        }
    }
}
