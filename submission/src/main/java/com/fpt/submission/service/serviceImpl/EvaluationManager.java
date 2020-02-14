package com.fpt.submission.service.serviceImpl;

import static com.fpt.submission.constants.CommonConstant.*;

import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.exception.CustomException;
import com.fpt.submission.utils.PathUtils;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.utils.CmdExcution;
import com.fpt.submission.utils.SubmissionUtils;
import com.fpt.submission.utils.ZipFile;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


@EnableAsync
@Service
public class EvaluationManager {

    private Boolean isEvaluating;
    private Queue<StudentSubmitDetail> submissionQueue;
    private List<String> examScriptsList;
    private Path sourceScriptPath = null;
    private Path serverTestScript = null;
    private String PREFIX_EXAM_SCRIPT = "EXAM_";
    private PathDetails pathDetails;

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
                String s = pathDetails.getPathTestScripts()+File.separator;
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

    @Async
    @EventListener
    public void evaluate(StudentSubmitDetail submissionEvent) {
        System.out.println(Thread.currentThread().getName() + "-" + submissionEvent.getStudentCode());
        submissionQueue.add(submissionEvent);
        if (!isEvaluating && submissionQueue.size() > 0) {
            isEvaluating = true;
            switch (pathDetails.getExamCode()) {
                case CODE_PRACTICAL_C:
                    evaluateSubmissionC(submissionQueue.remove());
                    break;
                case CODE_PRACTICAL_JAVA_WEB:
                    evaluateSubmissionJavaWeb(submissionQueue.remove());
                    break;
                case CODE_PRACTICAL_CSharp:
                    evaluateSubmissionCSharp(submissionQueue.remove());
                    break;
                case CODE_PRACTICAL_JAVA:
                    evaluateSubmissionJava(submissionQueue.remove());
                    break;
                default:
                    throw new CustomException(HttpStatus.NOT_FOUND, "Not found Path Details Exam code");
            }
        } else {
            Logger.getLogger(SubmissionUtils.class.getName())
                    .log(Level.ERROR, "[EVALUATE] Waiting - : " + submissionEvent.getStudentCode());
        }
    }


    private void evaluateSubmissionC(StudentSubmitDetail dto) {

    }

    private void evaluateSubmissionJava(StudentSubmitDetail dto) {
        try {

            Logger.getLogger(SubmissionUtils.class.getName())
                    .log(Level.INFO, "[EVALUATE] Student code : " + dto.getStudentCode());

            sourceScriptPath = null;
            serverTestScript = null;
            if (examScriptsList.size() == 0)
                throw new CustomException(HttpStatus.NOT_FOUND, "No exam codes");
            for (String scriptCode : examScriptsList) {
                if (scriptCode.equalsIgnoreCase(dto.getScriptCode() + ".java")) {
                    sourceScriptPath = Paths.get(pathDetails.getPathTestScripts() + File.separator + scriptCode);
                    serverTestScript = Paths.get(pathDetails.getPathTestJavaFol() + PREFIX_EXAM_SCRIPT + dto.getStudentCode() + "_" + scriptCode);
                    break;
                }
            }
            //copy source to target using Files Class
            if (sourceScriptPath == null && serverTestScript == null) {
                System.out.println("[PATH-SCRIPT-ERROR]" + dto.getStudentCode() + "-" + dto.getScriptCode());
                return;
            }
            Files.copy(sourceScriptPath, serverTestScript);
            ZipFile.unzip(pathDetails.getPathSubmission() + File.separator + dto.getStudentCode() + ".zip", pathDetails.getPathJavaSubmit());

            // Chạy CMD file test
            CmdExcution.execute(pathDetails.getJavaExecuteCmd());

            if (submissionQueue.size() > 0) {
                deleteAllFile(dto.getStudentCode(), pathDetails.getPathJavaSubmitDelete());
                evaluateSubmissionJava(submissionQueue.remove());
            } else {
                isEvaluating = false;
            }

            // Trả status đã chấm xong về app lec winform (mssv)
            System.out.println("Trả response cho giảng viên");
        } catch (Exception e) {
            Logger.getLogger(EvaluationManager.class.getName())
                    .log(Level.ERROR, "[EVALUATE-ERROR] Student code : " + dto.getStudentCode());
            e.printStackTrace();
        } finally {
            deleteAllFile(dto.getStudentCode(), pathDetails.getPathJavaSubmitDelete());
        }
    }

    private void evaluateSubmissionJavaWeb(StudentSubmitDetail dto) {

    }


    private void evaluateSubmissionCSharp(StudentSubmitDetail dto) {
        try {
            Logger.getLogger(SubmissionUtils.class.getName())
                    .log(Level.INFO, "[EVALUATE] Student code : " + dto.getStudentCode());

            sourceScriptPath = null;
            serverTestScript = null;
            if (examScriptsList.size() == 0)
                throw new CustomException(HttpStatus.NOT_FOUND, "No exam codes");
            for (String scriptCode : examScriptsList) {
                if (scriptCode.equalsIgnoreCase(dto.getScriptCode() + ".cs")) {
                    sourceScriptPath = Paths.get(pathDetails.getPathTestScripts() + File.separator + scriptCode);
                    serverTestScript = Paths.get(pathDetails.getPathTestCSharpFol() + PREFIX_EXAM_SCRIPT + dto.getStudentCode() + "_" + scriptCode);
                    break;
                }
            }
            //copy source to target using Files Class
            if (sourceScriptPath == null && serverTestScript == null) {
                System.out.println("[PATH-SCRIPT-ERROR]" + dto.getStudentCode() + "-" + dto.getScriptCode());
                return;
            }
            Files.copy(sourceScriptPath, serverTestScript);
            ZipFile.unzip(pathDetails.getPathSubmission() + File.separator + dto.getStudentCode() + ".zip", pathDetails.getPathCSharpSubmit());

            // Chạy CMD file test
            CmdExcution.execute(pathDetails.getCSharpExecuteCmd());

            if (submissionQueue.size() > 0) {
                deleteAllFile(dto.getStudentCode(), pathDetails.getPathCSharpSubmitDelete());
                evaluateSubmissionJava(submissionQueue.remove());
            } else {
                isEvaluating = false;
            }
            // Trả status đã chấm xong về app lec winform (mssv)
            System.out.println("Trả response cho giảng viên");
        } catch (Exception e) {
            Logger.getLogger(EvaluationManager.class.getName())
                    .log(Level.ERROR, "[EVALUATE-ERROR] Student code : " + dto.getStudentCode());
            e.printStackTrace();
        } finally {
            deleteAllFile(dto.getStudentCode(), pathDetails.getPathCSharpSubmitDelete());
        }
    }


    private void deleteAllFile(String studentCode, String pathSubmit) {

        File file = new File(pathSubmit);
        if (file != null && SubmissionUtils.deleteFolder(file)) {
            System.out.println("[DELETE SUBMISSION - SERVER] - " + studentCode);
        }

        File scriptFile = new File(serverTestScript.toString());
        if (scriptFile != null && scriptFile.delete()) {
            System.out.println("[DELETE SCRIPT - SERVER] - " + studentCode);
        }
    }

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // TODO: Xem xét lại khúc này coi pool nhiêu là đủ
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("[THREAD-EVALUATE]-");
        executor.initialize();
        return executor;
    }
}
