package com.example.demo.controller;

import com.example.demo.model.Keyword;
import com.example.demo.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 接口方法返回对象 转换为json文本
@RequestMapping("/api/keywords")
public class KeywordController {
    @Autowired
    private KeywordService keywordService;

    @PostMapping // method post
    public Keyword addKeyword(@RequestBody Keyword keyword){
        System.out.print("插入数据");
        return keywordService.addKeyword(keyword);
    }

    @DeleteMapping("/{id}")
    public void deleteKeyword(@PathVariable Long id){
        keywordService.deleteKeyword(id);
    }

    @PutMapping
    public Keyword updateKeyword(@RequestBody Keyword keyword){
        return keywordService.updateKeyword(keyword);
    }

    @GetMapping
    public List<Keyword> getAllKeywords(){
        return keywordService.getAllKeywords();
    }
}
