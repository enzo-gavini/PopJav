package com.enzo.persistenceservice.controller;

import com.enzo.persistenceservice.entity.Result;
import com.enzo.persistenceservice.service.ResultService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@AllArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping
    public Result save(@RequestBody Result result) {
        return resultService.create(result);
    }

    @GetMapping
    public List<Result> getAllResult() {
        return resultService.findAll();
    }

    @GetMapping("/{id}")
    public Result getResultById(@PathVariable Long id) {
        return resultService.findById(id);
    }

    @PutMapping("/{id}")
    public Result updateResult(@PathVariable Long id, @RequestBody Result result) {
        return resultService.updateResult(result);
    }

    @DeleteMapping("/{id}")
    public void deleteResult(@PathVariable Long id) {
        resultService.delete(id);
    }

}
