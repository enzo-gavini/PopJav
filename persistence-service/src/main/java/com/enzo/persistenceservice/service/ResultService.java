package com.enzo.persistenceservice.service;

import com.enzo.persistenceservice.entity.Result;
import com.enzo.persistenceservice.repository.ResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;

    public Result create(Result result) {
        return resultRepository.save(result);
    }

    public List<Result> findAll() {
        return resultRepository.findAll();
    }

    public Result findById(Long id) {
        return resultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result not found"));
    }

    public Result updateResult(Result result) {
        return resultRepository.save(result);
    }

    public void delete(Long id) {resultRepository.deleteById(id);}
}
