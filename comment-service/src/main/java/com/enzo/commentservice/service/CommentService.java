package com.enzo.commentservice.service;

import com.enzo.commentservice.entity.Comment;
import com.enzo.commentservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(String id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public Comment updateComment(Comment comment) {
        Comment existingComment = findById(comment.getId());
        existingComment.setText(comment.getText());
        return commentRepository.save(existingComment);
    }

    public void delete(String id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public List<Comment> findByLessonId(Long lessonid) {
        return commentRepository.findByLessonId(lessonid);
    }
}
