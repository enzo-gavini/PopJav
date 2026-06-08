package com.enzo.commentservice.repository;

import com.enzo.commentservice.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByLessonId(Long lessonId);
}
