package com.enzo.uiservice.controller;

import com.enzo.uiservice.dto.ResultDTO;
import com.enzo.uiservice.proxy.ResultFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/results")
@AllArgsConstructor
public class ResultController {
    private final ResultFeignClient resultFeignClient;

    @GetMapping
    public String showResults(Model model) {
        List<ResultDTO> results = resultFeignClient.getAllResult();
        model.addAttribute("results", results);
        return "results";
    }

    @GetMapping("/{id}")
    public String showResultById(Model model, @PathVariable Long id) {
        model.addAttribute("resultDTO", resultFeignClient.getResultById(id));
        return "result-detail";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("resultDTO", new ResultDTO());
        return "result-create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ("resultDTO") ResultDTO result) {
        resultFeignClient.save(result);
        return "redirect:/results";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("resultDTO", resultFeignClient.getResultById(id));
        return "result-edit";
    }

    @PostMapping("/edit/{id}")
    public  String updateResult(@PathVariable Long id, @ModelAttribute("resultDTO") ResultDTO result) {
        resultFeignClient.updateResult(id, result);
        return "redirect:/results";
    }

    @GetMapping("/delete/{id}")
    public String deleteResult(@PathVariable Long id) {
        resultFeignClient.deleteResult(id);
        return "redirect:/results";
    }


}
