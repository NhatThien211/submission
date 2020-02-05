package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.exception.CustomException;
import com.fpt.submission.utils.PathUtils;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.utils.CmdExcution;
import com.fpt.submission.utils.SubmissionUtils;
import com.fpt.submission.utils.ZipFile;
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

/**
 * Copyright 2019 {@author Loda} (https://loda.me).
 * This project is licensed under the MIT license.
 *
 * @since 2019-05-31
 * Github: https://github.com/loda-kun
 */
@EnableAsync
@Service
public class EvaluationManager {

    private Boolean isEvaluating;
    private Queue<StudentSubmitDetail> submissionQueue;
    private List<String> examCodesList;
    private Path sourceScriptPath = null;
    private Path serverTestScript = null;
    private String PREFIX_EXAM_SCRIPT = "EXAM_";
    private PathDetails pathDetails;

    @Autowired
    public EvaluationManager() {
        isEvaluating = false;
        submissionQueue = new PriorityQueue<>();
        pathDetails = PathUtils.pathDetails;
        examCodesList = getExamCodesList();
    }

    // Get all exams code in TestScript folder
    private List<String> getExamCodesList() {
        PathDetails pathDetails = PathUtils.pathDetails;
        List<String> result = null;
        if (pathDetails != null) {
            try {
                result = new ArrayList<>();
                String s  = pathDetails.getPathTestScripts();
                File folder = new File(pathDetails.getPathTestScripts());
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
            evaluateSubmissionJava(submissionQueue.remove());
        } else {
            System.out.println("[WAITING]" + submissionEvent.getStudentCode());
        }
    }

    private void evaluateSubmissionJava(StudentSubmitDetail dto) {
        try {
            sourceScriptPath = null;
            serverTestScript = null;
            if(examCodesList.size() ==0)
                throw new CustomException(HttpStatus.NOT_FOUND,"No exam codes");
            for (String examCode : examCodesList) {
                if (examCode.equalsIgnoreCase(dto.getExamCode() + ".java")) {
                    sourceScriptPath = Paths.get(pathDetails.getPathTestScripts() + File.separator + examCode);
                    serverTestScript = Paths.get(pathDetails.getPathTestFol() + PREFIX_EXAM_SCRIPT + dto.getStudentCode() + "_" + examCode);
                    break;
                }
            }
            //copy source to target using Files Class
            if (sourceScriptPath == null && serverTestScript == null) {
                System.out.println("[PATH-SCRIPT-ERROR]" + dto.getStudentCode() + "-" + dto.getExamCode());
                return;
            }
            Files.copy(sourceScriptPath, serverTestScript);
            ZipFile.unzip(pathDetails.getPathSubmission() + File.separator + dto.getStudentCode() + ".zip", pathDetails.getPathJavaFol());

            // Chạy CMD file test
            CmdExcution.execute(pathDetails.getJavaExecuteCmd());

            if (submissionQueue.size() > 0) {
                deleteAllFile(dto.getStudentCode());
                evaluateSubmissionJava(submissionQueue.remove());
            } else {
                isEvaluating = false;
            }

            // Trả status đã chấm xong về app lec winform (mssv)

            System.out.println("Trả response cho giảng viên");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             deleteAllFile(dto.getStudentCode());
        }
    }

    private void deleteAllFile(String studentCode) {
        File file = new File(pathDetails.getPathJavaComFol());
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
