package com.enzo.persistenceservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @ManyToOne
    @JoinColumn(name = "quizId")
    @JsonBackReference
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    @JsonManagedReference
    private List<Answer> answers;

    public Long getQuizId() {
        return quiz != null ? quiz.getId() : null;
    }
}
