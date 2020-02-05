package com.fpt.submission.controller;

import com.fpt.submission.utils.PathUtils;
import com.fpt.submission.dto.request.PracticalInfo;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.SubmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionUtils submissionUtils;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController( SubmissionUtils submissionUtils, SubmissionService submissionService) {
        this.submissionUtils = submissionUtils;
        this.submissionService = submissionService;
    }

    @GetMapping("/test")
    public void test() throws IOException {
        String s ="E:\\CN9\\FU_Submission_Webservice\\submission\\PracticalExams\\Practical_05022020\\Server";

    }

    @PostMapping("/submission")
    public String uploadFile(@ModelAttribute UploadFileDto file) throws IOException {
        return submissionService.submit(file);
    }

}
