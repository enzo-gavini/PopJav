package com.enzo.persistenceservice.service.dto;

import lombok.Data;

@Data
public class QuestionCreateDTO {
    private String text;
    private Long quizId;
}
