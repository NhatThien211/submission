package com.fpt.submission.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubmitDetail implements Serializable {
    private String studentCode;
    private String examCode;
}
