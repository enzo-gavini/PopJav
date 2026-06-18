package com.enzo.uiservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QuizSubmissionDTO {
    private Long quizId;
    private Long userId;
    private Map<Long, Long> answers;
}
