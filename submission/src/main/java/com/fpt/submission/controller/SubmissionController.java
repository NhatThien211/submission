package com.fpt.submission.controller;

import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.ScriptService;
import com.fpt.submission.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final ScriptService scriptService;
    private final UploadFile uploadFile;

    @Autowired
    public SubmissionController(ScriptService scriptService, UploadFile uploadFile) {
        this.scriptService = scriptService;
        this.uploadFile = uploadFile;
    }

    @PostMapping("/submission")
    public String uploadFile(@ModelAttribute UploadFileDto file) throws IOException {
        uploadFile.uploadFile(file);
        return "ok";
    }

    @GetMapping("/download")
    @CrossOrigin(origins = "http://localhost:1998")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        scriptService.downloadFile(response);
    }
}
