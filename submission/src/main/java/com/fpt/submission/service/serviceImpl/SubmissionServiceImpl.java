package com.fpt.submission.service.serviceImpl;

import com.fpt.submission.dto.request.StudentSubmitDetail;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.SubmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private SubmissionUtils submissionUtils;
    @Autowired
     ApplicationEventPublisher applicationEventPublisher;
    private boolean isDummy = false;
    private List<StudentSubmitDetail> listDummy;


    @Autowired
    public SubmissionServiceImpl() {
        submissionUtils = new SubmissionUtils();
        listDummy = new ArrayList<>();

    }

    @Override
    public String submit(UploadFileDto dto) {
        try {
            submissionUtils.submitSubmission(dto);
            applicationEventPublisher.publishEvent(new StudentSubmitDetail(
                    this, dto.getStudentCode(), dto.getScriptCode()));

//            if (!isDummy) {
//                dummyStudentSubmission();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Submit failed";
        }
        return "Submit successfully ";
    }

    private void dummyStudentSubmission() {
        listDummy.add(new StudentSubmitDetail(
                this, "SE61902", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE61616", "Java_439576447_DE01"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62289", "Java_439576447_DE01"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62792", "Java_439576449_DE02"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62804", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62823", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62841", "Java_439576447_DE01"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62867", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62878", "Java_439576450_DE01"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE62915", "Java_439576450_DE02"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63030", "Java_439576450_DE01"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63073", "Java_439576450_DE02"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63151", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63152", "Java_439576449_DE02"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63192", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63200", "Java_439576449_DE02"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63220", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63236", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63254", "Java_439576447_DE01"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63284", "Java_439576450_DE03"));
        listDummy.add(new StudentSubmitDetail(
                this, "SE63369", "Java_439576449_DE02"));

        // for dummy 21 students
        for (StudentSubmitDetail studentSubmitDetail : listDummy) {
            try {
                Thread.sleep(5000);
                applicationEventPublisher.publishEvent(new StudentSubmitDetail(
                        this, studentSubmitDetail.getStudentCode(), studentSubmitDetail.getScriptCode()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        isDummy = false;
    }

}
