package com.example.demo.controller;
import com.example.demo.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api")
public class FilterController {
    @Autowired
    private FilterService filterService;

    @GetMapping("/filter_and")
    public List<Map<String, Object>> filtedData_and(
            @RequestParam(value = "dataName", required = false) String dataName,
            @RequestParam("keywords") List<String> keywords){
        return filterService.filterDataByKeywords_and(dataName, keywords);
    }

    @GetMapping("/filter_or")
    public List<Map<String, Object>> filtedData_or(
            @RequestParam(value = "dataName", required = false) String dataName,
            @RequestParam("keywords") List<String> keywords){
        return filterService.filterDataByKeywords_or(dataName, keywords);
    }

    @GetMapping("/filter-by-rule")
    public List<Map<String, Object>> filtedData_byRule(
            @RequestParam(value = "dataName", required = false) String dataName,
            @RequestParam("logic") String logic,
            @RequestParam("keywords") List<String> keywords
    ){
        return filterService.filterDataByRule(dataName, logic, keywords);
    }
}
