package com.enzo.persistenceservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterSummaryDTO {
    private Long id;
    private String title;
    private String description;
    private int orderIndex;
}
