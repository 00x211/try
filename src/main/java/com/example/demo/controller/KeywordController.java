package com.example.demo.controller;

import com.example.demo.dto.KeywordCategoryDTO;
import com.example.demo.dto.KeywordDTO;
import com.example.demo.model.Keyword;
import com.example.demo.model.KeywordCategory;
import com.example.demo.service.KeywordService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 接口方法返回对象 转换为json文本
@CrossOrigin
@RequestMapping("/api/keywords")
public class KeywordController {
//    @Autowired
//    private KeywordService keywordService;
//
//    @PostMapping // method post
//    public Keyword addKeyword(@RequestBody Keyword keyword){
//        System.out.print("插入数据");
//        return keywordService.addKeyword(keyword);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteKeyword(@PathVariable Long id){
//        keywordService.deleteKeyword(id);
//    }
//
//    @PutMapping
//    public Keyword updateKeyword(@RequestBody Keyword keyword){
//        return keywordService.updateKeyword(keyword);
//    }
//
//    @GetMapping
//    public List<Keyword> getAllKeywords(){
//        return keywordService.getAllKeywords();
//    }
    @Autowired
    private KeywordService keywordService;

    /**
     * 获取所有一级分类及其二级分类和关键词
     *
     * @return 一级分类列表
     */
    @GetMapping
//    public List<KeywordCategory> getAllCategories() {
//        return keywordService.getAllCategories();
//    }
    public List<KeywordCategoryDTO> getAllCategories() {
        return keywordService.getAllCategories();
    }

    /**
     * 添加关键词
     *
     * @param request 请求体
     * @return 添加的关键词
     */
    @PostMapping("/add_keywords")
    public KeywordDTO addKeyword(@RequestBody KeywordRequest request) {
        return keywordService.addKeyword(request.getKeyword(), request.getFirstLevelId(), request.getSecondLevelId());
    }
    @PostMapping("/add_firstclass")
    public KeywordCategory addFirstClass(@RequestBody KeywordRequest request) {
        return keywordService.createFirstLevelCategory(request.getKeyword());
    }
    @PostMapping("add_secondclass")
    public KeywordCategoryDTO addSecondClass(@RequestBody KeywordRequest request) {
        return keywordService.createSecondLevelCategory(request.getKeyword(), request.getFirstLevelId());
    }

    /**
     * 删除一级分类
     *
     * @param firstLevelId 一级分类ID
     */
    @DeleteMapping("/first-level/{firstLevelId}")
    public void deleteFirstLevelCategory(@PathVariable Long firstLevelId) {
        keywordService.deleteFirstLevelCategory(firstLevelId);
    }

    /**
     * 删除二级分类
     *
     * @param secondLevelId 二级分类ID
     */
    @DeleteMapping("/second-level/{secondLevelId}")
    public void deleteSecondLevelCategory(@PathVariable Long secondLevelId) {
        keywordService.deleteSecondLevelCategory(secondLevelId);
    }

    /**
     * 删除关键词
     *
     * @param keywordId 关键词ID
     */
    @DeleteMapping("/keyword/{keywordId}")
    public void deleteKeyword(@PathVariable Long keywordId) {
        keywordService.deleteKeyword(keywordId);
    }

    /**
     * 修改一级分类名称
     *
     * @param firstLevelId 一级分类ID
     * @param newName 新名称
     * @return 修改后的一级分类
     */
    @PutMapping("/first-level/{firstLevelId}")
    public KeywordCategory updateFirstLevelCategory(
            @PathVariable Long firstLevelId,
            @RequestParam String newName) {
        return keywordService.updateFirstLevelCategory(firstLevelId, newName);
    }

    /**
     * 修改二级分类名称
     * @param secondLevelId 二级分类ID
     * @param newName 新名称
     * @return 修改后的二级分类
     */
    @PutMapping("/second-level/{secondLevelId}")
    public KeywordCategory updateSecondLevelCategory(
            @PathVariable Long secondLevelId,
            @RequestParam String newName) {
        return keywordService.updateSecondLevelCategory(secondLevelId, newName);
    }

    /**
     * 修改关键词名称
     * @param keywordId 关键词ID
     * @param newName 新名称
     * @return 修改后的关键词
     */
    @PutMapping("/keyword/{keywordId}")
    public Keyword updateKeyword(
            @PathVariable Long keywordId,
            @RequestParam String newName) {
        return keywordService.updateKeyword(keywordId, newName);
    }

    // 请求体类
    @Setter
    @Getter
    public static class KeywordRequest {
        // Getters and Setters
        private String keyword;
        private Long firstLevelId;
        private Long secondLevelId;
    }
}
