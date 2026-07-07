package com.enzo.quizservice.controller;

import com.enzo.quizservice.dto.ResultDTO;
import com.enzo.quizservice.service.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST endpoints for quiz results, relayed to persistence-service.
 * Enforces ownership and admin rights on reads by user.
 */
@RestController
@RequestMapping("/api/results")
@AllArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping
    public ResultDTO save(@RequestBody ResultDTO resultDTO) {
        return resultService.create(resultDTO);
    }

    @GetMapping
    public List<ResultDTO> getAllResult() {
        return resultService.findAll();
    }

    @GetMapping("/{id}")
    public ResultDTO getResultById(@PathVariable Long id) {
        return resultService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<ResultDTO> getResultsByUserId(@PathVariable Long userId,
                                              @RequestHeader("X-User-Id") Long callerId,
                                              @RequestHeader(value = "X-User-Role", required = false) String callerRole) {
        // A user can only read their own results; an admin can read anyone's.
        // Any other caller gets a 403 Access denied.
        if (!userId.equals(callerId) && !"ADMIN".equals(callerRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return resultService.findByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResultDTO updateResult(@PathVariable Long id, @RequestBody ResultDTO resultDTO) {
        return resultService.updateResult(id, resultDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteResult(@PathVariable Long id) {
        resultService.delete(id);
    }
}