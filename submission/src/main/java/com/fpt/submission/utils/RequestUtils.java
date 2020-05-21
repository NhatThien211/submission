package com.fpt.submission.utils;

import com.fpt.submission.dto.request.UploadFileDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.fpt.submission.constants.CommonConstant.URL_WEBSERVICE_SERVER;

public class RequestUtils {

    private static final int MAX_TIME = 10;

    public static void sendFile(String studentCode, String submitTime, String point, File project, String examCode) {
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("studentCode", studentCode);
        body.add("submitTime", submitTime);
        body.add("point", point);
        body.add("file", new FileSystemResource(project));
        body.add("examCode", examCode);

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body);


        RestTemplate restTemplate = new RestTemplate();
        int count = 0;
        resendRequest(restTemplate, requestEntity, count);
    }

    private static void resendRequest(RestTemplate restTemplate, HttpEntity<MultiValueMap<String, Object>> requestEntity, int count) {
        String serverUrl = URL_WEBSERVICE_SERVER;
        count++;
        ResponseEntity<String> response = restTemplate
                .postForEntity(serverUrl, requestEntity, String.class);
        if (response != null && (response.getStatusCode() != HttpStatus.OK) && count < MAX_TIME) {
            resendRequest(restTemplate, requestEntity, count);
        } else {
            System.out.println(response);
        }
    }

    private static File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convFile;
    }

}
