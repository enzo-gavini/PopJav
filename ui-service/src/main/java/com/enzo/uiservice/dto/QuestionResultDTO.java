package com.enzo.uiservice.dto;

import lombok.Data;

@Data
public class QuestionResultDTO {
    private Long questionId;
    private String questionText;
    private Long selectedAnswerId;
    private Long correctAnswerId;
    private boolean correct;
}