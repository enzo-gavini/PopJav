package com.enzo.persistenceservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A lesson inside a chapter, optionally paired with a quiz.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private int orderIndex;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonBackReference
    private Chapter chapter;

    @OneToOne(mappedBy = "lesson")
    @JsonManagedReference
    private Quiz quiz;

    public Long getChapterId() {
        return chapter != null ? chapter.getId() : null;
    }
}
