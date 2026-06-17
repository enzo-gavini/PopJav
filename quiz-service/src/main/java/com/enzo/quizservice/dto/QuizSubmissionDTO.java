package com.enzo.quizservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QuizSubmissionDTO {
    private Long quizId;
    private Long userId;
    private Map<Long, Long> answers;
}
