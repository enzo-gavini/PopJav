package com.enzo.persistenceservice.service.dto;

import lombok.Data;

@Data
public class QuizCreateDTO {
    private String title;
    private int lives;
    private int passingScore;
    private Long lessonId;
}
