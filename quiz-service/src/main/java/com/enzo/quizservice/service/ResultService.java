package com.enzo.quizservice.service;

import com.enzo.quizservice.dto.ResultDTO;
import com.enzo.quizservice.service.proxy.ResultFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResultService {
    private final ResultFeignClient resultFeignClient;

    public ResultDTO create(ResultDTO resultDTO) {
        return resultFeignClient.save(resultDTO);
    }

    public List<ResultDTO> findAll() {
        return resultFeignClient.getAllResult();
    }

    public ResultDTO findById(Long id) {
        return resultFeignClient.getResultById(id);
    }

    public List<ResultDTO> findByUserId(Long userId) {
        return resultFeignClient.getResultsByUserId(userId);
    }

    public ResultDTO updateResult(Long id, ResultDTO resultDTO) {
        return resultFeignClient.updateResult(id, resultDTO);
    }

    public void delete(Long id) {
        resultFeignClient.deleteResult(id);
    }
}