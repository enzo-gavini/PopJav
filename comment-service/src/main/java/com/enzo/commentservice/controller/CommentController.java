package com.enzo.commentservice.controller;

import com.enzo.commentservice.entity.Comment;
import com.enzo.commentservice.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public Comment save(@RequestBody Comment comment, @RequestHeader("X-User-Id") Long userId) {
        // Trust the identity from the gateway, not the client-supplied userId.
        comment.setUserId(userId);
        return commentService.save(comment);
    }

    @GetMapping
    public List<Comment> getAllComment() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable String id) {
        return commentService.findById(id);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable String id, @RequestBody Comment comment) {
        return commentService.updateComment(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id) {
        commentService.delete(id);
    }

    @GetMapping("/user")
    public List<Comment> findByUserId(@RequestParam Long userId) {
        return commentService.findByUserId(userId);
    }

    @GetMapping("/lesson")
    public List<Comment> findByLessonId(@RequestParam Long lessonId) {
        return commentService.findByLessonId(lessonId);
    }
}