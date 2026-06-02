package com.enzo.contentservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChapterDTO {
    private Long id;
    private String title;
    private String description;
    private int orderIndex;
    private List<LessonDTO> lessons;
}
