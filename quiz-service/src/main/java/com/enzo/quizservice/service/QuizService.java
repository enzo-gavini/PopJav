package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.*;
import com.enzo.quizservice.service.proxy.QuizFeignClient;
import com.enzo.quizservice.service.proxy.ResultFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizFeignClient quizFeignClient;
    private final ResultFeignClient resultFeignClient;

    public QuizDTO create(QuizDTO quizDTO) {
        return quizFeignClient.save(quizDTO);
    }

    public List<QuizDTO> findAll() {
        List<QuizDTO> quizzes = quizFeignClient.getAllQuiz();
        quizzes.forEach(this::hideCorrectAnswers);
        return quizzes;
    }

    public QuizDTO findById(Long id) {
        return hideCorrectAnswers(quizFeignClient.getQuizById(id));
    }

    // The quiz served to the client must never reveal which answer is correct
    // (a player could read it from the network payload). Scoring is done
    // server-side in submitQuiz, which fetches its own unmodified copy.
    private QuizDTO hideCorrectAnswers(QuizDTO quiz) {
        if (quiz != null && quiz.getQuestions() != null) {
            for (QuestionDTO question : quiz.getQuestions()) {
                if (question.getAnswers() != null) {
                    question.getAnswers().forEach(answer -> answer.setCorrect(false));
                }
            }
        }
        return quiz;
    }

    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        return quizFeignClient.updateQuiz(id, quizDTO);
    }

    public void delete(Long id) {
        quizFeignClient.deleteQuiz(id);
    }

    public QuizResultDTO submitQuiz(QuizSubmissionDTO submission) {

        QuizDTO quiz = quizFeignClient.getQuizById(submission.getQuizId());

        int score = 0;
        int lives = quiz.getLives();
        List<QuestionResultDTO> details = new ArrayList<>();

        for (QuestionDTO question : quiz.getQuestions()) {

            AnswerDTO correctAnswer = question.getAnswers().stream()
                    .filter(a -> a.isCorrect())
                    .findFirst()
                    .orElse(null);

            Long userAnswerId = submission.getAnswers().get(question.getId());

            boolean isCorrect = correctAnswer != null
                    && correctAnswer.getId().equals(userAnswerId);

            if (isCorrect) {
                score++;
            } else {
                lives--;
            }

            QuestionResultDTO detail = new QuestionResultDTO();
            detail.setQuestionId(question.getId());
            detail.setQuestionText(question.getText());
            detail.setSelectedAnswerId(userAnswerId);
            detail.setCorrectAnswerId(correctAnswer != null ? correctAnswer.getId() : null);
            detail.setCorrect(isCorrect);
            details.add(detail);

            if (lives <= 0) {
                break;
            }
        }

        int totalQuestions = quiz.getQuestions().size();
        int percentage = totalQuestions == 0 ? 0 : (score * 100) / totalQuestions;
        boolean passed = percentage >= quiz.getPassingScore();

        QuizResultDTO result = new QuizResultDTO();
        result.setScore(percentage);
        result.setTotalQuestions(totalQuestions);
        result.setPassed(passed);
        result.setLivesRemaining(lives);
        result.setDetails(details);

        ResultDTO savedResult = new ResultDTO();
        savedResult.setScore(percentage);
        savedResult.setCompleted(passed);
        savedResult.setAttempts(1);
        savedResult.setUserId(submission.getUserId());
        savedResult.setQuizId(submission.getQuizId());
        resultFeignClient.save(savedResult);

        return result;
    }
}