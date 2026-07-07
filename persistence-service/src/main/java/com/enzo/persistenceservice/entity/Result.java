package com.enzo.persistenceservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One user's attempt at a quiz: score, completion and attempt count.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int score;
    private boolean completed;
    private int attempts;
    // Plain Long on purpose, no JPA relation: the user lives in another context
    // (auth), so the link stays logical and the coupling loose.
    private Long userId;

    // @JsonBackReference breaks the infinite serialization loop
    // (Quiz -> results -> Result -> quiz -> ...).
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonBackReference
    private Quiz quiz;

    // The quiz field is hidden from JSON by @JsonBackReference: this exposes only
    // the id, and stays null-safe if the quiz is missing.
    public Long getQuizId() {
        return quiz != null ? quiz.getId() : null;
    }

}
