package com.enzo.contentservice.dto;

import lombok.Data;

@Data
public class QuizDTO {
    private Long id;
    private String title;
    private int lives;
    private int passingScore;
    private Long lessonId;
}