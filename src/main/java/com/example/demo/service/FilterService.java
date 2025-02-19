package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.example.demo.util.RuleParser;
@Service
public class FilterService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 同时满足关键词 用AND
    public List<Map<String, Object>> filterDataByKeywords_and(String dataName, List<String> keywords){
        if(dataName == null || dataName.isEmpty()){
            dataName = "test_dataset";
        }

        StringBuilder sqlBuilder = new StringBuilder("select * from "+dataName + " WHERE ");
        for(int i=0;i < keywords.size();i++){
            if(i>0){
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append("saved_data LIKE ?");
        }

        Object[] params = keywords.stream()
                .map(keyword -> "%" + keyword + "%")
                .toArray();

        return jdbcTemplate.queryForList(sqlBuilder.toString(), params);
    }

    // 包含一个即可 OR
    public List<Map<String, Object>> filterDataByKeywords_or(String dataName, List<String> keywords){
        if(dataName == null || dataName.isEmpty()){
            dataName = "test_dataset";
        }

        StringBuilder sqlBuilder = new StringBuilder("select * from "+dataName + " WHERE ");
        for(int i=0;i < keywords.size();i++){
            if(i>0){
                sqlBuilder.append(" OR ");
            }
            sqlBuilder.append("saved_data LIKE ?");
        }

        Object[] params = keywords.stream()
                .map(keyword -> "%" + keyword + "%")
                .toArray();

        return jdbcTemplate.queryForList(sqlBuilder.toString(), params);
    }

    /**
     * 查询符合规则的数据
     *
     * @param dataName 数据表名称
     * @param logic    逻辑语法（与、或、非）
     * @param keywords 关键词列表
     * @return 查询结果
     */
    public List<Map<String, Object>> filterDataByRule(String dataName, String logic, List<String> keywords) {
        // 如果未传入 dataName，则使用默认表 test_data
        if (dataName == null || dataName.isEmpty()) {
            dataName = "test_data";
        }

        // 将逻辑语法和关键词转化为正则表达式
        String regex = RuleParser.toRegex(logic, keywords);

        // 构建 SQL 查询语句
        String sql = "SELECT * FROM " + dataName + " WHERE saved_data REGEXP ?";

        // 执行查询
        return jdbcTemplate.queryForList(sql, regex);
    }
}
