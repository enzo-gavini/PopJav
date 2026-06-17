package com.enzo.quizservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnswerDTO {
    private Long id;
    private String text;
    private boolean correct;
    private Long questionId;
    List<AnswerDTO> answers;
}
