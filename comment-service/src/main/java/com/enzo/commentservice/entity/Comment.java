package com.enzo.commentservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * A comment on a lesson, stored in MongoDB. userId and lessonId are logical
 * references: no foreign key is possible between MongoDB and PostgreSQL.
 */
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
