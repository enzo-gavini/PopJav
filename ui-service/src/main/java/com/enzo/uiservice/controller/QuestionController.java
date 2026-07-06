package com.enzo.uiservice.controller;


import com.enzo.uiservice.dto.QuestionDTO;
import com.enzo.uiservice.proxy.QuestionFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles the question pages.
 */
@Controller
@RequestMapping("/questions")
@AllArgsConstructor
public class QuestionController {
    private final QuestionFeignClient questionFeignClient;

    @GetMapping
    public String showQuestions(Model model) {
        List<QuestionDTO> questions = questionFeignClient.getAllQuestion();
        model.addAttribute("questions", questions);
        return "questions";
    }

    @GetMapping("/{id}")
    public String showQuestionById(Model model, @PathVariable Long id) {
        model.addAttribute("questionDTO", questionFeignClient.getQuestionById(id));
        return "question-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("questionDTO", new QuestionDTO());
        return "question-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ("questionDTO") QuestionDTO question) {
        questionFeignClient.save(question);
        return "redirect:/questions";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("questionDTO", questionFeignClient.getQuestionById(id));
        return "question-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateQuestion(@PathVariable Long id, @ModelAttribute("questionDTO") QuestionDTO question) {
        questionFeignClient.updateQuestion(id, question);
        return "redirect:/questions";
    }

    @GetMapping("/delete/{id}")
    public  String deleteQuestion(@PathVariable Long id) {
        questionFeignClient.deleteQuestion(id);
        return "redirect:/questions";
    }
}
