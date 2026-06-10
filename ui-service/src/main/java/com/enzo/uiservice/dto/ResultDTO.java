package com.enzo.uiservice.dto;

import lombok.Data;

@Data
public class ResultDTO {
    private Long id;
    private int score;
    private boolean completed;
    private int attempts;
    private Long userId;
    private Long quizId;
}
