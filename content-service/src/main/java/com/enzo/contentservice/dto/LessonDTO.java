package com.enzo.contentservice.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private int orderIndex;
    private Long chapterId;

}
