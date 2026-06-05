package com.enzo.quizservice.service.proxy;

import com.enzo.quizservice.dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "persistence-service", contextId = "resultClient", path = "/api/results")
public interface ResultFeignClient {

    @PostMapping
    public ResultDTO save(@RequestBody ResultDTO resultDTO);

    @GetMapping
    public List<ResultDTO> getAllResult();

    @GetMapping("/{id}")
    public ResultDTO getResultById(@PathVariable Long id);

    @PutMapping("/{id}")
    public ResultDTO updateResult(@PathVariable Long id, @RequestBody ResultDTO resultDTO);

    @DeleteMapping("/{id}")
    public void deleteResult(@PathVariable Long id);
}
