package com.enzo.quizservice.dto;

import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private Long quizId;
}
