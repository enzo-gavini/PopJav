package com.enzo.persistenceservice.service.dto;


import lombok.Data;

@Data
public class LessonCreateDTO {
    private String title;
    private String content;
    private int orderIndex;
    private Long chapterId;
}
