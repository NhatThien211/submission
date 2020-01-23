package com.fpt.submission.service;

import com.fpt.submission.dto.request.UploadFileDto;

public interface SubmissionService {
    String submit(UploadFileDto dto);
}
