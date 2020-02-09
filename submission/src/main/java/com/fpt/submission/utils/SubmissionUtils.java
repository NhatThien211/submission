package com.fpt.submission.utils;

import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.exception.CustomException;
import com.fpt.submission.service.serviceImpl.EvaluationManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@EnableAsync
@Service
public class SubmissionUtils {

    private EvaluationManager evaluationManager;

    public SubmissionUtils() {
        evaluationManager = new EvaluationManager();
    }

    @Bean("ThreadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("[THREAD-SUBMIT]-");
        return executor;
    }

    @Async("ThreadPoolTaskExecutor")
    public Boolean submitSubmission(UploadFileDto dto) {
        try {
            Logger.getLogger(SubmissionUtils.class.getName())
                    .log(Level.INFO, "[SUBMISSION] - File from student: " + dto.getStudentCode());
            MultipartFile file = dto.getFile();
            if (file != null) {
                PathDetails pathDetails = PathUtils.pathDetails;

                String folPath = pathDetails.getPathSubmission();
                Path copyLocation = Paths.get(folPath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(SubmissionUtils.class.getName())
                    .log(Level.ERROR, "[SUBMISSION-ERROR] - File from student : "+ ex.getMessage());
            throw new CustomException(HttpStatus.CONFLICT, ex.getMessage());
        }
        return false;
    }

    public static boolean deleteFolder(File directory) {
        //make sure directory exists
        if (!directory.exists()) {
            Logger.getLogger(SubmissionUtils.class.getName())
                    .log(Level.WARN, "[DELETE FOLDER] - : Directory does not exist");
        } else {
            File[] allContents = directory.listFiles();
            if (allContents != null) {
                for (File file : allContents) {
                    deleteFolder(file);
                }
            }
        }
        return directory.delete();
    }


}
