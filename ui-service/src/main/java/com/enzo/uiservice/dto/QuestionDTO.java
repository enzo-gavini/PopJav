package com.enzo.uiservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String text;
    private Long quizId;
    List<AnswerDTO> answers;
}
