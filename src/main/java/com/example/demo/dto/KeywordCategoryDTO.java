package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class KeywordCategoryDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

//    // 自定义 parent 字段
//    private ParentCategoryDTO parent;
//
//    @Data
//    public static class ParentCategoryDTO {
//        private Long id;
//        private String name;
//    }
    // 二级分类列表
    private List<SecondLevelCategoryDTO> secondLevelCategories;

    @Data
    public static class SecondLevelCategoryDTO {
        private Long id;
        private String name;

        // 关键词列表
        private List<KeywordDTO> keywords;
    }

    @Data
    public static class KeywordDTO {
        private Long id;
        private String name;
        private LocalDateTime createdAt;
    }
}