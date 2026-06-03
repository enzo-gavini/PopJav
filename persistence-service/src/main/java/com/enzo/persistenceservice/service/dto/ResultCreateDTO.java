package com.enzo.persistenceservice.service.dto;

import lombok.Data;

@Data
public class ResultCreateDTO {
    private int score;
    private boolean completed;
    private int attempts;
    private Long userId;
    private Long quizId;
}
