package com.fpt.submission.dto.request;


import org.springframework.context.ApplicationEvent;

import java.io.Serializable;


public class StudentSubmitDetail extends ApplicationEvent implements Serializable {
    private String studentCode;
    private String examCode;

    public StudentSubmitDetail(Object source, String studentCode, String examCode) {
        super(source);
        this.studentCode = studentCode;
        this.examCode = examCode;
    }

    public String getExamCode() {
        return examCode;
    }

    public String getStudentCode() {
        return studentCode;
    }
}
