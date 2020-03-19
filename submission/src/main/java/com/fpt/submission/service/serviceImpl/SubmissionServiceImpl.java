package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.CommonConstant;
import com.fpt.submission.dto.request.PathDetails;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.PathUtils;
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
                    this, dto.getStudentCode(), dto.getScriptCode()));
            String submissionMsg = dto.getStudentCode() + "T" + SubmissionUtils.getCurTime();
            new Thread() {
                int count = 0;
                public void run() {
                    while (count < 5) {
                        try {
                            SubmissionUtils.sendTCPMessage(submissionMsg, CommonConstant.SOCKET_SERVER_LOCAL_HOST, CommonConstant.SOCKET_SERVER_LISTENING_PORT_SUBMISSION);
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            count++;
                        }
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
            return "Submit failed";
        }

        return "Submit successfully ";
    }


}
