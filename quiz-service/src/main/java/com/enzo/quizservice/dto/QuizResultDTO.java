package com.enzo.quizservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizResultDTO {
    private int score;
    private int totalQuestions;
    private boolean passed;
    private int livesRemaining;
    private List<QuestionResultDTO> details; }
