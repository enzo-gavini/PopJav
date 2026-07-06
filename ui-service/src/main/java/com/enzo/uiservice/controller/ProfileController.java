package com.enzo.uiservice.controller;

import com.enzo.uiservice.dto.QuizDTO;
import com.enzo.uiservice.dto.ResultDTO;
import com.enzo.uiservice.dto.UserDTO;
import com.enzo.uiservice.proxy.QuizFeignClient;
import com.enzo.uiservice.proxy.ResultFeignClient;
import com.enzo.uiservice.proxy.UserFeignClient;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays the profile page: user info and quiz stats aggregated from several services.
 */
@Controller
@AllArgsConstructor
public class ProfileController {
    private final UserFeignClient userFeignClient;
    private final ResultFeignClient resultFeignClient;
    private final QuizFeignClient quizFeignClient;

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        Long userId = (Long) session.getAttribute("userId");
        UserDTO user = userFeignClient.getUserByEmail(email);
        List<ResultDTO> results = resultFeignClient.getResultsByUserId(userId);

        // Fallback to "Quiz #id" if a quiz was deleted: the profile page must not crash.
        Map<Long, String> quizNames = new HashMap<>();
        for (ResultDTO result : results) {
            if (!quizNames.containsKey(result.getQuizId())) {
                try {
                    QuizDTO quiz = quizFeignClient.getQuizById(result.getQuizId());
                    quizNames.put(result.getQuizId(), quiz.getTitle());
                } catch (Exception e) {
                    quizNames.put(result.getQuizId(), "Quiz #" + result.getQuizId());
                }
            }
        }

        long totalQuizzes = results.size();
        long quizzesPassed = results.stream().filter(ResultDTO::isCompleted).count();

        model.addAttribute("user", user);
        model.addAttribute("results", results);
        model.addAttribute("quizNames", quizNames);
        model.addAttribute("totalQuizzes", totalQuizzes);
        model.addAttribute("quizzesPassed", quizzesPassed);
        return "profile";
    }
}