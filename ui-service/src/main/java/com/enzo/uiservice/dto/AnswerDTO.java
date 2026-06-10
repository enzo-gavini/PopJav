package com.enzo.uiservice.dto;

import lombok.Data;

@Data
public class AnswerDTO {
    private Long id;
    private String text;
    private boolean correct;
    private Long questionId;
}
