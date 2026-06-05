package com.enzo.persistenceservice.service.dto;

import lombok.Data;

@Data
public class AnswerCreateDTO {
    private String text;
    private boolean correct;
    private Long questionId;
}
