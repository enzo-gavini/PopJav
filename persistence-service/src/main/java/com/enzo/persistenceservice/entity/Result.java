package com.enzo.persistenceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

}
