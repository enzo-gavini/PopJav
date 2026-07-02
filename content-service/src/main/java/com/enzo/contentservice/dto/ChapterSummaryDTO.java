package com.enzo.contentservice.dto;

import lombok.Data;

@Data
public class ChapterSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private int orderIndex;
}
