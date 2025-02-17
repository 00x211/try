package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
}
