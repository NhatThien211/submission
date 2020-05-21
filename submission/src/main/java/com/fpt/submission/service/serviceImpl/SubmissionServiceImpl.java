package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.CommonConstant;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.Queue;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private SubmissionUtils submissionUtils;
    private Queue<String> submitTimeQueue;
    private boolean isSendingTCP = false;
    private static final int MAX_TIME = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionServiceImpl.class);

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public SubmissionServiceImpl() {
        submissionUtils = new SubmissionUtils();
        submitTimeQueue = new PriorityQueue<>();
    }

    @Override
    public String submit(UploadFileDto dto) {
        try {
            submissionUtils.submitSubmission(dto);
            applicationEventPublisher.publishEvent(new StudentSubmitDetail(
                    this, dto.getStudentCode(), dto.getScriptCode()));

            submitTimeQueue.add(dto.getStudentCode());
            if (!isSendingTCP && submitTimeQueue.size() > 0) {
                isSendingTCP = true;
                int count = 0;
                sendTCPTimeSubmit(submitTimeQueue.remove(), count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Submit failed";
        }

        return "Submit successfully ";
    }

    public void sendTCPTimeSubmit(String studentCode, int count) {
        if (count < MAX_TIME) {
            try {
                count++;
                String time = studentCode + "T" + SubmissionUtils.getCurTime();
                SubmissionUtils.sendTCPMessage(time, CommonConstant.SOCKET_SERVER_LOCAL_HOST, CommonConstant.SOCKET_SERVER_LISTENING_PORT_SUBMISSION);
                if (submitTimeQueue.size() > 0) {
                    sendTCPTimeSubmit(submitTimeQueue.remove(), count);
                    Thread.sleep(1000);
                } else {
                    isSendingTCP = false;
                }
            } catch (Exception e) {
                LOGGER.info("[RE-SEND TCP SUBMIT TIME]:" + count);
                sendTCPTimeSubmit(studentCode, count);
            }
        }else{
            LOGGER.info("[REFUSE-SEND TCP SUBMIT TIME]");
        }
    }
}
