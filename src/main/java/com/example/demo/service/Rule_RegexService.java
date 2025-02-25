package com.example.demo.service;
import com.example.demo.dao.Rule_RegexRepository;
import com.example.demo.model.Rule_Regex;
import com.example.demo.model.RuleStatistics;
import com.example.demo.dao.RuleStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class Rule_RegexService {
    @Autowired
    private Rule_RegexRepository rule_regexRepository;

    @Autowired
    private RuleStatisticsRepository ruleStatisticsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 创建规则
     *
     * @param ruleName    规则名称
     * @param description 规则描述
     * @param regex       正则表达式
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return 创建的规则
     */
    public Rule_Regex createRule(String ruleName, String description, List<String> regex, LocalDate startDate, LocalDate endDate) {
        Rule_Regex rule = new Rule_Regex();
        rule.setRuleName(ruleName);
        rule.setDescription(description);
        rule.setRegex(regex);
        rule.setStartDate(startDate);
        rule.setEndDate(endDate);

        return rule_regexRepository.save(rule);
    }

    // 查找
    public List<Rule_Regex> getAllRules() {
        return rule_regexRepository.findAll();
    }

    // 根据名称ruleName查找
    public Rule_Regex getRuleByName(String ruleName) {
        return rule_regexRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new RuntimeException("规则不存在:" + ruleName));
    }

    // 根据id获取
    public Rule_Regex getRuleById(Long id) {
        return rule_regexRepository.getById(id);
    }

    // 删除
    @Transactional
    public void deleteRule(Long id) {
        Rule_Regex rule = rule_regexRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("规则不存在,id:" + id));
        rule_regexRepository.delete(rule);
    }

    // 更新
    public Rule_Regex updateRule(Long id, Rule_Regex rule_regex) {
        Rule_Regex rule = rule_regexRepository.findById(id).orElseThrow(() -> new RuntimeException("规则不存在,id:" + id));
        rule.setRuleName(rule_regex.getRuleName());
        rule.setDescription(rule_regex.getDescription());
        rule.setRegex(rule_regex.getRegex());
        rule.setStartDate(rule_regex.getStartDate());
        rule.setEndDate(rule_regex.getEndDate());
        return rule_regexRepository.save(rule);
    }
    /**
     * 应用规则
     *
     * @param ruleName 规则名称
     * @param dataName 目标数据表名称
     * @return 筛选结果表名
     */
    @Transactional
    public List<RuleStatistics> applyRule(String ruleName, String dataName) {
        if (dataName == null || dataName.isEmpty()) {
            dataName = "test_dataset";
        }

        // 获取规则
        Rule_Regex rule = rule_regexRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new RuntimeException("规则不存在: " + ruleName));

        // 获取目标数据表的原始数据数目
        int originCounts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM `" + dataName + "`", Integer.class);

        // 构建查询条件
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM `" + dataName + "` WHERE saved_data REGEXP ");
        List<String> regexs = rule.getRegex();
        for(int i = 0; i < regexs.size(); i++) {
//            queryBuilder.append(regexs.get(i));
//            if(i != regexs.size() - 1) {
//                queryBuilder.append(" AND saved_data REGEXP ");
//            }
            String regex = regexs.get(i);
            // 判断是否以单引号开头和结尾
            if (regex.startsWith("'") && regex.endsWith("'")) {
                queryBuilder.append(regex);
            } else {
                queryBuilder.append("'").append(regex).append("'");
            }
            if(i != regexs.size() - 1) {
                queryBuilder.append(" AND saved_data REGEXP ");
            }
        }
        if (rule.getStartDate() != null) {
            queryBuilder.append(" AND saved_date >= '").append(rule.getStartDate()).append("'");
        }
        if (rule.getEndDate() != null) {
            queryBuilder.append(" AND saved_date <= '").append(rule.getEndDate()).append("'");
        }

        // 执行查询
        List<Map<String, Object>> filteredData = jdbcTemplate.queryForList(queryBuilder.toString());

        // 创建新表存储筛选结果
        String filteredTableName = "filtered_by_" + ruleName + "_on_" + dataName + "_" + System.currentTimeMillis();
        jdbcTemplate.execute("CREATE TABLE `" + filteredTableName + "` AS " + queryBuilder.toString());

        // 获取筛选出的数据数目
        int counts = filteredData.size();

        // 保存统计信息
        RuleStatistics statistics = new RuleStatistics();
        statistics.setTableName(filteredTableName);
        statistics.setCounts(counts);
        statistics.setOriginCounts(originCounts);
        statistics.setRuleName(ruleName);
        statistics.setDataName(dataName);
        ruleStatisticsRepository.save(statistics);

        return ruleStatisticsRepository.findAll();
    }
}
