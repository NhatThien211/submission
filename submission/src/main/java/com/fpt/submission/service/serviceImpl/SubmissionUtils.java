package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.CommonConstant;
import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.exception.CustomException;
import com.fpt.submission.utils.PathUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@EnableAsync
@Service
public class SubmissionUtils {

    private EvaluationManager evaluationManager;
    private PathDetails pathDetails;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SubmissionUtils.class);


    public SubmissionUtils() {
        evaluationManager = new EvaluationManager();
        pathDetails = PathUtils.pathDetails;
    }

    @Bean(name = "ThreadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setBeanName("ThreadPoolTaskExecutor");
        executor.setCorePoolSize(30);
        executor.setMaxPoolSize(100);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("[THREAD-SUBMIT]-");
        return executor;
    }

    @Async("ThreadPoolTaskExecutor")
    public void submitSubmission(UploadFileDto dto) {
        try {
            LOGGER.info("[SUBMISSION] - File from student: " + dto.getStudentCode());
            MultipartFile file = dto.getFile();

//            dto.setSubjectCode("JAVA");
            // Send file to check duplicated code
//            sendFile(dto);

            if (file != null) {
                PathDetails pathDetails = PathUtils.pathDetails;
                String folPath = pathDetails.getPathSubmission();
                Path copyLocation = Paths.get(folPath + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            if (pathDetails.getPracticalExamCode().contains(CommonConstant.CODE_PRACTICAL_JAVA_WEB)) {
                MultipartFile webFile = dto.getWebFile();
                if (webFile != null) {
                    String folPath = pathDetails.getPathSubmission();
                    Path copyLocation = Paths.get(folPath + File.separator + StringUtils.cleanPath(webFile.getOriginalFilename()));
                    Files.copy(webFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("[SUBMISSION-ERROR] - File from student : " + ex.getMessage());
            throw new CustomException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

//    private void sendFile(UploadFileDto dto) {
//        MultiValueMap<String, Object> body
//                = new LinkedMultiValueMap<>();
//        body.add("studentCode", dto.getStudentCode());
//        body.add("file", new FileSystemResource(convert(dto.getFile())));
//        body.add("examCode", pathDetails.getPracticalExamName());
//
//        RestTemplate restTemplate = new RestTemplate();
//        int count = 0;
//        if(response!=null){
//            response.getStatusCode()
//            System.out.println(response);
//        }
//    }
//
//    private void resendRequest(RestTemplate restTemplate,){
//        String serverUrl = URL_WEBSERVICE_SERVER;
//        ResponseEntity<String> response = restTemplate
//                .postForEntity(serverUrl, requestEntity, String.class);
//    }

    private File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {

        }

        return convFile;
    }

    public static boolean deleteFolder(File directory) {
        //make sure directory exists
        if (directory.exists()) {
            File[] allContents = directory.listFiles();
            if (allContents != null) {
                for (File file : allContents) {
                    deleteFolder(file);
                }
            }
        }
        return directory.delete();
    }

    public static void sendTCPMessage(String message, String serverHost, int serverPort) throws IOException {
        Socket clientSocket = null;
        BufferedWriter bw = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;

        try {
            // make a connection with server
            clientSocket = new Socket(serverHost, serverPort);

            os = clientSocket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            bw.write(message);
            bw.flush();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (osw != null) {
                    osw.close();
                }
                if (os != null) {
                    os.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCurTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static boolean saveResultCSubmission(String result, String filePath) {
        // TODO: For re-submit
        File file = null;
        PrintWriter writer = null;
        try {
            file = new File(filePath);
            writer = new PrintWriter(new FileWriter(file, true));
            writer.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            writer.close();
        }
        return true;
    }

}
