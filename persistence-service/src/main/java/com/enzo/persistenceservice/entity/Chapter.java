package com.enzo.persistenceservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A chapter groups an ordered list of lessons.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int orderIndex;

    // Parent side: @JsonManagedReference serializes the lessons; the child side
    // cuts the loop (see Quiz).
    @OneToMany(mappedBy = "chapter")
    @JsonManagedReference
    private List<Lesson> lessons;
}
