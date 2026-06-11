package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.CommentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "api-gateway", contextId = "commentClient", path = "/api/comments")
public interface CommentFeignClient {

    @PostMapping
    public CommentDTO save(@RequestBody CommentDTO commentDTO);

    @GetMapping
    public List<CommentDTO> getAllComment();

    @GetMapping("/{id}")
    public CommentDTO getCommentById(@PathVariable String id);

    @PutMapping("/{id}")
    public CommentDTO updateComment(@PathVariable String id, @RequestBody CommentDTO commentDTO);

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id);

    @GetMapping("/user")
    public List<CommentDTO> findByUserId(@RequestParam Long userId);

    @GetMapping("/lesson")
    public List<CommentDTO> findByLessonId(@RequestParam Long lessonId);
}
