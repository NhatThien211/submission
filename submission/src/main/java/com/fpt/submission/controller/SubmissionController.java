package com.fpt.submission.controller;

import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.ScriptService;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.SubmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final ScriptService scriptService;
    private final SubmissionUtils submissionUtils;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(ScriptService scriptService, SubmissionUtils submissionUtils, SubmissionService submissionService) {
        this.scriptService = scriptService;
        this.submissionUtils = submissionUtils;
        this.submissionService = submissionService;
    }

    @PostMapping("/submission")
    public String uploadFile(@ModelAttribute UploadFileDto file) throws IOException {
        return submissionService.submit(file);
    }

    @GetMapping("/download")
    @CrossOrigin(origins = "http://localhost:1998")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        scriptService.downloadFile(response);
    }
}
