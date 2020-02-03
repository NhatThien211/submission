package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.constants.PathConstants;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.CmdExcution;
import com.fpt.submission.utils.SubmissionUtils;
import com.fpt.submission.utils.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionUtils submissionUtils;

    @Autowired
    public SubmissionServiceImpl() {
        this.submissionUtils = new SubmissionUtils();
        //TODO: Lấy thêm danh sách đề
    }

    @Override
    public String submit(UploadFileDto dto) {
        try {
            submissionUtils.evaluateSubmission(dto);
        } catch (Exception e) {
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
