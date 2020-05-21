package com.fpt.submission.controller;

import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.service.serviceImpl.SubmissionUtils;
import com.fpt.submission.utils.CmdExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionUtils submissionUtils;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionUtils submissionUtils, SubmissionService submissionService) {
        this.submissionUtils = submissionUtils;
        this.submissionService = submissionService;
    }


    @PostMapping("/submission")
    public String submit(@ModelAttribute UploadFileDto file) {
        return submissionService.submit(file);
    }
}
