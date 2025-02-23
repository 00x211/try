package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KeywordDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    // 自定义 category 字段
    private CategoryDTO category;

    @Data
    public static class CategoryDTO {
        private Long id;
        private String name;
    }
}