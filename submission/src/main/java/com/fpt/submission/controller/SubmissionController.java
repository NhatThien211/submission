package com.fpt.submission.controller;

import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.utils.UploadFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SubmissionController {
    @PostMapping("/submission")
    public String uploadFile(@ModelAttribute UploadFileDto file) throws IOException {
        UploadFile.uploadFile(file);
        return "ok";
    }
}
