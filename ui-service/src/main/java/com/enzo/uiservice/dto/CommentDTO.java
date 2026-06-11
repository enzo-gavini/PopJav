package com.enzo.uiservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private String id;
    private String text;
    private Long userId;
    private Long lessonId;
    private LocalDateTime createdAt;
}