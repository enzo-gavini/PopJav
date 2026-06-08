package com.enzo.commentservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Comment {
    @Id
    private String id;
    private String text;
    private Long userId;
    private Long lessonId;
    private LocalDateTime createdAt = LocalDateTime.now();
}
