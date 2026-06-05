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
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    private int lives;
    private int passingScore;

    @OneToOne
    @JoinColumn(name = "lesson_id")
    @JsonBackReference
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz")
    @JsonManagedReference
    private List<Question> questions;

    @OneToMany(mappedBy = "quiz")
    @JsonManagedReference
    private List<Result> results;

    public Long getLessonId() {
        return lesson != null ? lesson.getId() : null;
    }
}
