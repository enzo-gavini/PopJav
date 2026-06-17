package com.enzo.quizservice.dto;

import java.util.List;

public class QuizResultDTO {
    private int score;
    private int totalQuestions;
    private boolean passed;
    private int livesRemaining;
    private List<QuestionResultDTO> details; }
