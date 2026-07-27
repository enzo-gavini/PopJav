package com.enzo.uiservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class CommentDTO {
    private String id;
    private String text;
    private Long userId;
    private Long lessonId;
    private LocalDateTime createdAt;

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("d MMMM yyyy 'à' HH'h'mm", Locale.FRENCH);

    /**
     * The date as the reader sees it, "13 juillet 2026 à 09h53", instead of the
     * raw ISO timestamp. @JsonIgnore keeps it out of the JSON sent back to
     * comment-service, which only knows the createdAt field.
     */
    @JsonIgnore
    public String getDisplayDate() {
        return createdAt == null ? "" : createdAt.format(DISPLAY_FORMAT);
    }
}
