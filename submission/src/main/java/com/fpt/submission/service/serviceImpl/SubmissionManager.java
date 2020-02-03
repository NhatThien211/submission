package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.PathConstants;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.utils.CmdExcution;
import com.fpt.submission.utils.SubmissionUtils;
import com.fpt.submission.utils.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class SubmissionManager {

    private Boolean isEvaluating = false;
    private Queue<StudentSubmitDetail> submissionQueue;

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // TODO: Xem xét lại khúc này coi pool nhiêu là đủ
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Evaluate-");
        executor.initialize();
        return executor;
    }

    @Autowired
    public SubmissionManager() {
        isEvaluating = false;
        submissionQueue = new PriorityQueue<>();
    }


//        @EventListener sẽ lắng nghe mọi sự kiện xảy ra
//        Nếu có một sự kiện DoorBellEvent được bắn ra, nó sẽ đón lấy
//        và đưa vào hàm để xử lý

//        @Async là cách lắng nghe sự kiện ở một Thread khác, không ảnh hưởng tới
//        luồng chính

    @Async
    public void evaluate(StudentSubmitDetail submissionEvent) {
        submissionQueue.add(submissionEvent);
        if (!isEvaluating && submissionQueue.size() > 0) {
            isEvaluating = true;
            evaluateSubmission(submissionQueue.remove());
        }
    }

    private void evaluateSubmission(StudentSubmitDetail dto) {
        try {
            File folder = new File(PathConstants.PATH_JAVA_TEST);
            Path sourceScriptPath = null;
            Path serverTestScript = null;
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    switch (dto.getExamCode()) {
                        case "DE1":
                            sourceScriptPath = Paths.get(PathConstants.PATH_JAVA_TEST + File.separator + "De1.java");
                            serverTestScript = Paths.get(PathConstants.PATH_JAVA_FOLDER_TEST + "De1.java");
                            break;
                        case "DE2":
                            sourceScriptPath = Paths.get(PathConstants.PATH_JAVA_TEST + File.separator + "De2.java");
                            serverTestScript = Paths.get(PathConstants.PATH_JAVA_FOLDER_TEST + "De2.java");
                            break;
                    }
                    //copy source to target using Files Class
                    Files.copy(sourceScriptPath, serverTestScript);
                    break;
                }
            }

            ZipFile.unzip(PathConstants.PATH_JAVA_WEB_SUBMIT + File.separator + dto.getStudentCode() + ".zip", PathConstants.PATH_JAVA_FOLDER);

            // Chạy CMD file test
            CmdExcution.execute(PathConstants.EXECUTE_MAVEN_CMD);
            File file = new File(PathConstants.PATH_JAVA_FOLDER_COM);
            if (SubmissionUtils.deleteFolder(file)) {
                System.out.println("[Xóa bài làm server] - " + dto.getStudentCode());
            }

            File scriptFile = new File(serverTestScript.toString());
            if (scriptFile.delete()) {
                System.out.println("[Xóa file script server] - " + dto.getStudentCode());
            }

            if (submissionQueue.size() > 0) {
                evaluateSubmission(submissionQueue.remove());
            } else {
                isEvaluating = false;
            }

            // Trả status đã chấm xong về app lec winform (mssv)

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
}
