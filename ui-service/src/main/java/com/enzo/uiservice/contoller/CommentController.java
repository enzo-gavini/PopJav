package com.enzo.uiservice.contoller;

import com.enzo.uiservice.dto.CommentDTO;
import com.enzo.uiservice.proxy.CommentFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentFeignClient commentFeignClient;

    @GetMapping
    public String showComments(Model model) {
        List<CommentDTO> comments = commentFeignClient.getAllComment();
        model.addAttribute("comments", comments);
        return "comments";
    }

    @GetMapping("/{id}")
    public String showCommentById(Model model, @PathVariable String id) {
        model.addAttribute("commentDTO", commentFeignClient.getCommentById(id));
        return "comment-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("commentDTO", new CommentDTO());
        return "comment-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("commentDTO") CommentDTO comment) {
        commentFeignClient.save(comment);
        return "redirect:/lessons/" + comment.getLessonId();
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable String  id, Model model) {
        model.addAttribute("commentDTO", commentFeignClient.getCommentById(id));
        return "comment-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable String id, @ModelAttribute("commentDTO") CommentDTO comment) {
        commentFeignClient.updateComment(id, comment);
        return "redirect:/comments";
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable String id, @RequestParam Long lessonId) {
        commentFeignClient.deleteComment(id);
        return "redirect:/lessons/" + lessonId;
    }

    @GetMapping("/user")
    public String showCommentsByUser(@RequestParam Long userId, Model model) {
        model.addAttribute("comments", commentFeignClient.findByUserId(userId));
        return "comments";
    }

    @GetMapping("/lesson")
    public String showCommentsByLesson(@RequestParam Long lessonId, Model model) {
        model.addAttribute("comments", commentFeignClient.findByLessonId(lessonId));
        return "comments";
    }
}
