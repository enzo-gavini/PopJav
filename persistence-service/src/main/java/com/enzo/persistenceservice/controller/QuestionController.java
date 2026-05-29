    package com.enzo.persistenceservice.controller;

    import com.enzo.persistenceservice.entity.Question;
    import com.enzo.persistenceservice.service.QuestionService;
    import lombok.AllArgsConstructor;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/questions")
    @AllArgsConstructor

    public class QuestionController {
        private final QuestionService questionService;

        @PostMapping
        public Question save(@RequestBody Question question) {
            return questionService.create(question);
        }

        @GetMapping
        public List<Question> getAllQuestion() {
            return questionService.findAll();
        }

        @GetMapping("/{id}")
        public Question getQuestionById(@PathVariable Long id) {
            return questionService.findById(id);
        }

        @PutMapping("/{id}")
        public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
            return questionService.updateQuestion(question);
        }

        @DeleteMapping("/{id}")
        public void deleteQuestion(@PathVariable Long id) {
            questionService.delete(id);
        }
    }
