package com.fpt.submission.controller;

import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.ScriptService;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final ScriptService scriptService;
    private final FileUtils fileUtils;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(ScriptService scriptService, FileUtils fileUtils, SubmissionService submissionService) {
        this.scriptService = scriptService;
        this.fileUtils = fileUtils;
        this.submissionService = submissionService;
    }

    @PostMapping("/submission")
    public String uploadFile(@ModelAttribute UploadFileDto file) throws IOException {
        return submissionService.submit(file);
//        System.out.println(PathConstants.PROJECT_DIR);
//        return null;
    }

    @GetMapping("/download")
    @CrossOrigin(origins = "http://localhost:1998")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        scriptService.downloadFile(response);
    }
}
