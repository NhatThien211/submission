package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.PathConstants;
import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.CmdExcution;
import com.fpt.submission.utils.SubmissionUtils;
import com.fpt.submission.utils.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private SubmissionUtils submissionUtils;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public SubmissionServiceImpl() {
        submissionUtils = new SubmissionUtils();
        //TODO: Lấy thêm danh sách đề
    }

    @Override
    public String submit(UploadFileDto dto) {
        try {
            submissionUtils.submitSubmission(dto);
            applicationEventPublisher.publishEvent(new StudentSubmitDetail(
                    this,dto.getStudentCode(),dto.getExamCode()));
        } catch (Exception e) {
            e.printStackTrace();
            return "Submit failed";
        }
        return "Submit successfully ";
    }


    public void extractFolder(String path, String destDirectory) throws IOException, InterruptedException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                ZipFile.unzip(file.getAbsolutePath(), destDirectory);
                CmdExcution.execute(PathConstants.EXECUTE_MAVEN_CMD);
            }
        }
    }
}
