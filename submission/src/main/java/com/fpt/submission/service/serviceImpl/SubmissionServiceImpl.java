package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.CommonConstant;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.SubmissionUtils;
import org.apache.tomcat.util.bcel.classfile.Constant;
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
                    this, dto.getStudentCode(), dto.getScriptCode()));
            //send submission message
            String submissionMessage = dto.getStudentCode()+"T"+submissionUtils.getCurTime();
            submissionUtils.sendTCPMessage(submissionMessage, CommonConstant.SOCKET_SERVER_LOCAL_HOST,CommonConstant.SOCKET_SERVER_LISTENING_PORT_SUBMISSION);
        } catch (Exception e) {
            e.printStackTrace();
            return "Submit failed";
        }
        return "Submit successfully ";
    }

}
