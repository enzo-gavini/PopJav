package com.enzo.uiservice.controller;

import com.enzo.uiservice.dto.QuizDTO;
import com.enzo.uiservice.dto.QuizResultDTO;
import com.enzo.uiservice.dto.QuizSubmissionDTO;
import com.enzo.uiservice.proxy.QuizFeignClient;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/quizzes")
@AllArgsConstructor
public class QuizController {
    private final QuizFeignClient quizFeignClient;

    @GetMapping
    public String showQuiz(Model model) {
        List<QuizDTO> quizzes = quizFeignClient.getAllQuiz();
        model.addAttribute("quizzes", quizzes);
        return "quizzes";
    }

    @GetMapping("/{id}")
    public String showQuizById(Model model, @PathVariable Long id) {
        model.addAttribute("quizDTO", quizFeignClient.getQuizById(id));
        return "quiz-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("quizDTO", new QuizDTO());
        return "quiz-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ("quizDTO") QuizDTO quiz) {
        quizFeignClient.save(quiz);
        return "redirect:/quizzes";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id,  Model model) {
        model.addAttribute("quizDTO", quizFeignClient.getQuizById(id));
        return "quiz-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute("quizDTO") QuizDTO quiz){
        quizFeignClient.updateQuiz(id, quiz);
        return "redirect:/quizzes";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuiz(@PathVariable Long id) {
        quizFeignClient.deleteQuiz(id);
        return "redirect:/quizzes";
    }

    @GetMapping("/play/{id}")
    public String playQuiz(@PathVariable Long id, Model model) {
        QuizDTO quiz = quizFeignClient.getQuizById(id);
        model.addAttribute("quiz", quiz);
        model.addAttribute("submission", new QuizSubmissionDTO());
        return "quiz-play";
    }

    @PostMapping("/play/{id}")
    public String submitQuiz(@PathVariable Long id, @ModelAttribute QuizSubmissionDTO submission, HttpSession session, Model model) {
        submission.setQuizId(id);
        submission.setUserId((Long) session.getAttribute("userId"));
        QuizResultDTO result = quizFeignClient.submitQuiz(submission);
        model.addAttribute("result", result);
        return "quiz-result";
    }
}
