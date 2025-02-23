package com.example.demo.service;

import com.example.demo.dao.KeywordCategoryRepository;
import com.example.demo.dao.KeywordRepository;
import com.example.demo.dto.KeywordDTO;
import com.example.demo.dto.KeywordCategoryDTO;
import com.example.demo.model.Keyword;
import com.example.demo.model.KeywordCategory;
import jakarta.servlet.ServletConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KeywordService {
    @Autowired
    private KeywordCategoryRepository keywordCategoryRepository;

    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private ServletConfig servletConfig;

    //    public Keyword addKeyword(Keyword keyword){
//        return keywordRepository.save(keyword);
//    }
//    public void deleteKeyword(Long id){
//        keywordRepository.deleteById(id);
//    }
//    public Keyword updateKeyword(Keyword keyword){
//        return keywordRepository.save(keyword);
//    }
//    public List<Keyword> getAllKeywords(){
//        return keywordRepository.findAll();
//    }
    // 获取所有一级分类、二级和关键词
//    public List<KeywordCategory> getAllCategories() {
//        // 获取所有的一级标题
//        List<KeywordCategory> firstLevelCategories = keywordCategoryRepository.findByParentIsNull();
//
//        // 加载二级和关键词
//        return firstLevelCategories.stream().map(category -> {
//            category.setSecondLevelCategories(keywordCategoryRepository.findByParentId(category.getId()));
//            category.getSecondLevelCategories().forEach(secondLevelCategory -> {
//                secondLevelCategory.setKeywords(keywordRepository.findByCategoryId(secondLevelCategory.getId()));
//            });
//            return category;
//        }).collect(Collectors.toList());
//    }
    public List<KeywordCategoryDTO> getAllCategories() {
        // 获取所有一级分类
        List<KeywordCategory> firstLevelCategories = keywordCategoryRepository.findByParentIsNull();

        // 转换为 DTO
        return firstLevelCategories.stream()
                .map(this::convertToKeywordCategoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将 KeywordCategory 转换为 KeywordCategoryDTO
     */
    private KeywordCategoryDTO convertToKeywordCategoryDTO(KeywordCategory category) {
        KeywordCategoryDTO dto = new KeywordCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());

        // 加载二级分类
        List<KeywordCategory> secondLevelCategories = keywordCategoryRepository.findByParentId(category.getId());
        dto.setSecondLevelCategories(secondLevelCategories.stream()
                .map(this::convertToSecondLevelCategoryDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * 将二级分类转换为 DTO
     */
    private KeywordCategoryDTO.SecondLevelCategoryDTO convertToSecondLevelCategoryDTO(KeywordCategory category) {
        KeywordCategoryDTO.SecondLevelCategoryDTO dto = new KeywordCategoryDTO.SecondLevelCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        // 加载关键词
        dto.setKeywords(keywordRepository.findByCategoryId(category.getId()).stream()
                .map(keyword -> {
                    KeywordCategoryDTO.KeywordDTO keywordDTO = new KeywordCategoryDTO.KeywordDTO();
                    keywordDTO.setId(keyword.getId());
                    keywordDTO.setName(keyword.getName());
                    keywordDTO.setCreatedAt(keyword.getCreatedAt());
                    return keywordDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }

    // 添加关键词
    public KeywordDTO addKeyword(String keywordName, Long firstLevelId, Long secondLevelId){
        KeywordCategory secondLevelCategory = keywordCategoryRepository.findById(secondLevelId)
                .orElseThrow(() -> new RuntimeException("二级分类不存在: " + secondLevelId));

        Keyword keyword = new Keyword();
        keyword.setName(keywordName);
        keyword.setCategory(secondLevelCategory);
        keyword.setCreatedAt(LocalDateTime.now());

        Keyword savedKeyword = keywordRepository.save(keyword);
        return convertToKeywordDTO(savedKeyword);
//        return keywordRepository.save(keyword);
    }

    // 创建一级类别
    public KeywordCategory createFirstLevelCategory(String categoryName) {
        KeywordCategory firstLevelCategory = new KeywordCategory();
        firstLevelCategory.setName(categoryName);
        firstLevelCategory.setParent(null); // 一级类别没有父类别
        firstLevelCategory.setCreatedAt(LocalDateTime.now());

        return keywordCategoryRepository.save(firstLevelCategory);
    }

    // 创建二级类别
    public KeywordCategoryDTO createSecondLevelCategory(String categoryName, Long firstLevelId) {
        KeywordCategory firstLevelCategory = keywordCategoryRepository.findById(firstLevelId)
                .orElseThrow(() -> new RuntimeException("一级分类不存在: " + firstLevelId));

        KeywordCategory secondLevelCategory = new KeywordCategory();
        secondLevelCategory.setName(categoryName);
        secondLevelCategory.setParent(firstLevelCategory);
        secondLevelCategory.setCreatedAt(LocalDateTime.now());

        KeywordCategory savedCategory = keywordCategoryRepository.save(secondLevelCategory);
        return convertToKeywordCategoryDTO(savedCategory);
//        return keywordCategoryRepository.save(secondLevelCategory);
    }

    @Transactional
    public void deleteFirstLevelCategory(Long firstLevelId) {
        KeywordCategory firstLevelCategory = keywordCategoryRepository.findById(firstLevelId)
                .orElseThrow(() -> new RuntimeException("一级分类不存在: " + firstLevelId));

        // 删除一级分类下的二级分类和关键词
        List<KeywordCategory> secondLevelCategories = keywordCategoryRepository.findByParentId(firstLevelId);
        for (KeywordCategory secondLevelCategory : secondLevelCategories) {
            keywordRepository.deleteByCategoryId(secondLevelCategory.getId()); // 删除关键词
            keywordCategoryRepository.delete(secondLevelCategory); // 删除二级分类
        }

        // 删除一级分类
        keywordCategoryRepository.delete(firstLevelCategory);
    }

    @Transactional
    public void deleteSecondLevelCategory(Long secondLevelId) {
        KeywordCategory secondLevelCategory = keywordCategoryRepository.findById(secondLevelId)
                .orElseThrow(() -> new RuntimeException("二级分类不存在: " + secondLevelId));

        // 删除二级分类下的关键词
        keywordRepository.deleteByCategoryId(secondLevelId);

        // 删除二级分类
        keywordCategoryRepository.delete(secondLevelCategory);
    }

    @Transactional
    public void deleteKeyword(Long keywordId) {
        keywordRepository.deleteById(keywordId);
    }

    public KeywordCategory updateFirstLevelCategory(Long firstLevelId, String newName) {
        KeywordCategory firstLevelCategory = keywordCategoryRepository.findById(firstLevelId)
                .orElseThrow(() -> new RuntimeException("一级分类不存在: " + firstLevelId));

        firstLevelCategory.setName(newName);
        return keywordCategoryRepository.save(firstLevelCategory);
    }

    public KeywordCategory updateSecondLevelCategory(Long secondLevelId, String newName) {
        KeywordCategory secondLevelCategory = keywordCategoryRepository.findById(secondLevelId)
                .orElseThrow(() -> new RuntimeException("二级分类不存在: " + secondLevelId));

        secondLevelCategory.setName(newName);
        return keywordCategoryRepository.save(secondLevelCategory);
    }

    public Keyword updateKeyword(Long keywordId, String newName) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new RuntimeException("关键词不存在: " + keywordId));

        keyword.setName(newName);
        return keywordRepository.save(keyword);
    }

//    /**
//     * 将 KeywordCategory 转换为 KeywordCategoryDTO
//     */
//    private KeywordCategoryDTO convertToKeywordCategoryDTO(KeywordCategory category) {
//        KeywordCategoryDTO dto = new KeywordCategoryDTO();
//        dto.setId(category.getId());
//        dto.setName(category.getName());
//        dto.setCreatedAt(category.getCreatedAt());
//
//        if (category.getParent() != null) {
//            KeywordCategoryDTO.ParentCategoryDTO parentDTO = new KeywordCategoryDTO.ParentCategoryDTO();
//            parentDTO.setId(category.getParent().getId());
//            parentDTO.setName(category.getParent().getName());
//            dto.setParent(parentDTO);
//        }
//
//        return dto;
//    }
//
    /**
     * 将 Keyword 转换为 KeywordDTO
     */
    private KeywordDTO convertToKeywordDTO(Keyword keyword) {
        KeywordDTO dto = new KeywordDTO();
        dto.setId(keyword.getId());
        dto.setName(keyword.getName());
        dto.setCreatedAt(keyword.getCreatedAt());

        if (keyword.getCategory() != null) {
            KeywordDTO.CategoryDTO categoryDTO = new KeywordDTO.CategoryDTO();
            categoryDTO.setId(keyword.getCategory().getId());
            categoryDTO.setName(keyword.getCategory().getName());
            dto.setCategory(categoryDTO);
        }

        return dto;
    }
}
