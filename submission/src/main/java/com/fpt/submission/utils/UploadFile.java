package com.fpt.submission.utils;

import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.exception.CustomException;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@EnableAsync
public class UploadFile {
    private static final String JAVA_CONSOLE_FOLDER_PATH = "\\practical\\JavaConsole";

    @Bean("ThreadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    @Async("ThreadPoolTaskExecutor")
    public static void uploadFile(UploadFileDto dto) {
        try {
            MultipartFile file = dto.getFile();
            if (file != null) {
                String folPath = ResourceUtils.getFile("classpath:static" + JAVA_CONSOLE_FOLDER_PATH).getAbsolutePath();
                Path copyLocation = Paths.get(folPath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new CustomException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }
}
