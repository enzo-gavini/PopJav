package com.enzo.quizservice.dto;

import java.util.Map;

public class QuizSubmissionDTO {
    private Long quizId;
    private Long userId;
    private Map<Long, Long> answers;
}
