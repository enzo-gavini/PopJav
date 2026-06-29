package com.enzo.uiservice.proxy;

import com.enzo.uiservice.dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "api-gateway", contextId = "resultClient", path = "/api/results")
public interface ResultFeignClient {

    @PostMapping
    ResultDTO save(@RequestBody ResultDTO resultDTO);

    @GetMapping
    List<ResultDTO> getAllResult();

    @GetMapping("/{id}")
    ResultDTO getResultById(@PathVariable Long id);

    @GetMapping("/user/{userId}")
    List<ResultDTO> getResultsByUserId(@PathVariable Long userId);

    @PutMapping("/{id}")
    ResultDTO updateResult(@PathVariable Long id, @RequestBody ResultDTO resultDTO);

    @DeleteMapping("/{id}")
    void deleteResult(@PathVariable Long id);
}