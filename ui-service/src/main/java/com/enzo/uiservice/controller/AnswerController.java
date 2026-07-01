package com.enzo.uiservice.controller;

import com.enzo.uiservice.dto.AnswerDTO;
import com.enzo.uiservice.proxy.AnswerFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/answers")
@AllArgsConstructor
public class AnswerController {
    private final AnswerFeignClient answerFeignClient;

    @GetMapping
    public String showAnswers(Model model) {
        List<AnswerDTO> answers = answerFeignClient.getAllAnswer();
        model.addAttribute("answers", answers);
        return "answers";
    }

    @GetMapping("/{id}")
    public String showAnswerById(Model model, @PathVariable Long id) {
        model.addAttribute("answerDTO", answerFeignClient.getAnswerById(id));
        return "answer-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("answerDTO", new AnswerDTO());
        return "answer-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("answerDTO") AnswerDTO answer) {
        answerFeignClient.save(answer);
        return "redirect:/answers";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("answerDTO", answerFeignClient.getAnswerById(id));
        return "answer-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateAnswer(@PathVariable Long id, @ModelAttribute("answerDTO") AnswerDTO answer) {
        answerFeignClient.updateAnswer(id, answer);
        return "redirect:/answers";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnswer(@PathVariable Long id) {
        answerFeignClient.deleteAnswer(id);
        return "redirect:/answers";
    }
}
