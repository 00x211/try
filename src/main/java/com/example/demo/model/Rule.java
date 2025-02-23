// 这是基于drl文件的可以进行查询的规则表
// id，规则名称，描述，与逻辑，非逻辑，创建时间，更新时间

package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
//@Data
//@Entity
//public class Rule {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true, name = "rule_name")
//    @JsonProperty("rule_name")
//    private String ruleName; // 规则名称
//
//    @Column(nullable = false, name = "rule_content")
//    @JsonProperty("rule_content")
//    private String ruleContent; // 规则内容（DRL 格式）
//
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate(){
//        createdAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate(){
//        updatedAt = LocalDateTime.now();
//    }
//}
@Table(name = "rules")
@Data
@Entity
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "rule_name")
    @JsonProperty("rule_name")
    private String ruleName; // 规则名称

    @Column(nullable = true, name = "description")
    private String description; // 规则描述

    @Convert(converter = LogitsConverter.class)
    private List<List<String>> dataFilterLogitsAnd; // 数据过滤的逻辑-与操作

    @Convert(converter = LogitsConverter.class)
    private List<List<String>> dataFilterLogitsNot; // 数据过滤的逻辑-非操作

    @Column(name = "data_filter_logits_timestart")
    private LocalDate dataFilterLogitsTimestart;

    @Column(name = "data_filter_logits_timeend")
    private LocalDate dataFilterLogitsTimeend;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}

