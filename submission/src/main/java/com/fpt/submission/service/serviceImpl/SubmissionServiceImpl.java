package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.SubmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private SubmissionUtils submissionUtils;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public SubmissionServiceImpl() {
        submissionUtils = new SubmissionUtils();
    }

    @Override
    public String submit(UploadFileDto dto) {
        try {
            submissionUtils.submitSubmission(dto);
            applicationEventPublisher.publishEvent(new StudentSubmitDetail(
                    this, dto.getStudentCode(), dto.getScriptCode(), dto.getExamCode()));
        } catch (Exception e) {
            e.printStackTrace();
            return "Submit failed";
        }
        return "Submit successfully ";
    }

}
