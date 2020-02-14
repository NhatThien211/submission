package com.fpt.submission.dto.request;

import java.io.Serializable;
import java.util.Map;

class StudentPointDto implements Serializable {

    private String studentCode;
    private Map<String, String> listQuestions;
    private String totalPoint;
    private String submitTime;
    private String evaluateTime;
    private Double codingConvention;
    private String result;

    public StudentPointDto() {
    }

    public StudentPointDto(String studentCode, Map<String, String> listQuestions, String totalPoint, String submitTime, String evaluateTime, Double codingConvention, String result) {
        this.studentCode = studentCode;
        this.listQuestions = listQuestions;
        this.totalPoint = totalPoint;
        this.submitTime = submitTime;
        this.evaluateTime = evaluateTime;
        this.codingConvention = codingConvention;
        this.result = result;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Map<String, String> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(Map<String, String> listQuestions) {
        this.listQuestions = listQuestions;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public Double getCodingConvention() {
        return codingConvention;
    }

    public void setCodingConvention(Double codingConvention) {
        this.codingConvention = codingConvention;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}