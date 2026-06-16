package com.enzo.uiservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizDTO {
    private Long id;
    private String title;
    private int lives;
    private int passingScore;
    private Long lessonId;
    private List<QuestionDTO> questions;
}
