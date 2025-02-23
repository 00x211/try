package com.example.demo.controller;
//import com.example.demo.model.Rule;
//import com.example.demo.service.RuleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rules")
//public class RuleController {
//    @Autowired
//    private RuleService ruleService;
//
//    @PostMapping
//    public Rule addRule(@RequestBody Rule rule){
//        return ruleService.addRule(rule);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteRule(@PathVariable Long id){
//        ruleService.deleteRule(id);
//    }
//
//    @PutMapping
//    public Rule updateRule(@RequestBody Rule rule){
//        return ruleService.updateRule(rule);
//    }
//
//    @GetMapping
//    public List<Rule> getAllRules(){
//        return ruleService.getAllRules();
//    }
//}
import com.example.demo.dao.RuleStatisticsRepository;
import com.example.demo.model.Rule;
import com.example.demo.model.RuleStatistics;
import com.example.demo.model.SavedData;
import com.example.demo.service.RuleService;
import com.example.demo.model.Rule_Regex;
import com.example.demo.service.Rule_RegexService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
class ApplyRuleRequest {
    // Getter 和 Setter
    private String dataName;
    private String ruleName;

}

@RestController
@CrossOrigin
@RequestMapping("/api/rules")
public class RuleController {
    // 以下内容适配drl单独保存方案
    @Autowired
    private RuleService ruleService;

    @PostMapping
    public Rule addRule(@RequestBody Rule rule) throws IOException{
        return ruleService.addRule(rule);
    }

    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
    }

    @PutMapping
    public Rule updateRule(@RequestBody Rule rule) {
        return ruleService.updateRule(rule);
    }

    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/{ruleName}")
    public Rule getRuleByName(@PathVariable String ruleName) {
        return ruleService.getRuleByName(ruleName);
    }

    @PostMapping("/apply")
    public List<SavedData> applyRule(@RequestBody ApplyRuleRequest ruleRequest) {
        return ruleService.applyRule(ruleRequest.getDataName(), ruleRequest.getRuleName());
    }

    // 下面内容适配使用正则表达式的方案
    @Autowired
    private Rule_RegexService rule_regexService;
    // 创建规则
    @PostMapping("/create_regex")
    public Rule_Regex createRuleRegex(@RequestBody Rule_Regex rule_regex) {
        return rule_regexService.createRule(rule_regex.getRuleName(),
                rule_regex.getDescription(),rule_regex.getRegex(),
                rule_regex.getStartDate(),rule_regex.getEndDate());
    }
    // 删除基于正则的规则
    @DeleteMapping("/del_regex/{id}")
    public void deleteRuleRegex(@PathVariable Long id) {
        rule_regexService.deleteRule(id);
    }
    // 查询所有基于正则的规则
    @GetMapping("/find_regex")
    public List<Rule_Regex> getAllRulesRegex() {
        return rule_regexService.getAllRules();
    }
    // 根据 ID 查询基于正则的规则, 这个方法无法使用！！！
    @GetMapping("/find_regex/{id}")
    public Rule_Regex getRuleRegexById(@PathVariable Long id) {
        return rule_regexService.getRuleById(id);
    }
    // 修改规则
    @PutMapping("/update_regex/{id}")
    public Rule_Regex updateRuleRegex(@PathVariable Long id, @RequestBody Rule_Regex rule_regex) {
        return rule_regexService.updateRule(id,rule_regex);
    }
    // 应用规则
    @PostMapping("/apply_regex")
    public List<RuleStatistics> applyRuleRegex(@RequestBody ApplyRuleRequest ruleRequest) {
        return rule_regexService.applyRule(ruleRequest.getRuleName(),ruleRequest.getDataName());
    }
    @Autowired
    private RuleStatisticsRepository ruleStatisticsRepository;
    // 统计信息
    @GetMapping("/statistics")
    public List<RuleStatistics> getRuleStatistics() {
        return ruleStatisticsRepository.findAll();
    }
    // 展开数据的过程
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("/filtered_result")
    public List<Map<String, Object>> zhankai(@RequestParam String tableName){
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            return row;
        });
    }

}