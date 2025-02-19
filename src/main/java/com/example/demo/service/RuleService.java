package com.example.demo.service;
//import com.example.demo.dao.RuleRepository;
//import com.example.demo.model.Rule;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class RuleService {
//    @Autowired
//    private RuleRepository ruleRepository;
//    public Rule addRule(Rule rule){ // 增加规则
//        return ruleRepository.save(rule);
//    }
//    public void deleteRule(Long id){ // 使用id来删除规则（前端应该返回id）
//        ruleRepository.deleteById(id);
//    }
//    public Rule updateRule(Rule rule){ // 修改规则 暂时使用保存新规则，应该是把旧规则修改
//        return ruleRepository.save(rule);
//    }
//    public List<Rule> getAllRules(){ // 查找所有的规则
//        return ruleRepository.findAll();
//    }
//
//}
import com.example.demo.model.Rule;
import com.example.demo.dao.RuleRepository;
import com.example.demo.model.RuleGenerator;
import com.example.demo.model.SavedData;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    private static  final String RULES_DIR = "src/main/resources/rules_drl/";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KieContainer kieContainer;


    public Rule addRule(Rule rule) throws IOException {
        String ruleContent = RuleGenerator.generateDrl(rule);
        String rulePath = RULES_DIR + rule.getRuleName() + ".drl";
        Path path = Paths.get(rulePath);
        Files.write(path, ruleContent.getBytes());

        return ruleRepository.save(rule);
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }

    public Rule updateRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public Rule getRuleByName(String ruleName) {
        return ruleRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new RuntimeException("规则不存在: " + ruleName));
    }

    public List<SavedData> applyRule(String dataName, String ruleName) {
        if (dataName == null || dataName.isEmpty()) {
            dataName = "test_dataset";
        }

//        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM " + dataName);
//        List<SavedData> dataList = new ArrayList<>();
//        for (Map<String, Object> row : rows) {
//            SavedData data = new SavedData();
//            data.setId((Long) row.get("id"));
//            data.setSaved_data((String) row.get("saved_data"));
//            data.setSaved_date(((java.sql.Date) row.get("saved_date")).toLocalDate());
//            dataList.add(data);
//        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM " + dataName);
        List<SavedData> dataList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            SavedData data = new SavedData();

            // 修复 id 字段的类型转换问题
            data.setId(((Number) row.get("id")).longValue());

            // 修复 saved_date 字段的类型转换问题
            Object dateObj = row.get("saved_date");
            if (dateObj instanceof java.sql.Timestamp) {
                data.setSaved_date(((java.sql.Timestamp) dateObj).toLocalDateTime().toLocalDate());
            } else if (dateObj instanceof java.sql.Date) {
                data.setSaved_date(((java.sql.Date) dateObj).toLocalDate());
            } else {
                throw new IllegalArgumentException("Unexpected date type: " + dateObj);
            }

            data.setSaved_data((String) row.get("saved_data"));
            dataList.add(data);
        }

//        静态方法，把rules_drl的所有规则都执行了
//        KieSession kieSession = kieContainer.newKieSession("ksession-rules");
//        List<SavedData> result = new ArrayList<>();
//        kieSession.setGlobal("result", result);
//        for(SavedData data : dataList) {
//            kieSession.insert(data);
//        }
//        kieSession.fireAllRules();
//        kieSession.dispose();
//        createFilteredTable(ruleName, dataName);
//        saveFilteredData(ruleName, dataName, result);

        // 动态加载指定的 .drl 文件
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        try {
            // 读取指定的 .drl 文件内容
            String drlContent = new String(Files.readAllBytes(Paths.get("src/main/resources/rules_drl/" + ruleName + ".drl")));
            kieFileSystem.write(ResourceFactory.newByteArrayResource(drlContent.getBytes()).setTargetPath(ruleName + ".drl"));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        // 构建 KieModule
        KieModule kieModule = kieServices.newKieBuilder(kieFileSystem).buildAll().getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());

        // 创建 KieSession 并执行规则
        KieSession kieSession = kieContainer.newKieSession();
        List<SavedData> result = new ArrayList<>();
        kieSession.setGlobal("result", result);
        for (SavedData data : dataList) {
            kieSession.insert(data);
        }
        kieSession.fireAllRules();
        kieSession.dispose();
//        createFilteredTable(ruleName, dataName);
//        saveFilteredData(ruleName, dataName, result);

        return result;
    }

    private void createFilteredTable(String ruleName, String dataName) {
        String tableName = "filtered_by_" + ruleName + "_on_" + dataName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "saved_data TEXT NOT NULL, " +
                "saved_date DATE NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        jdbcTemplate.execute(sql);
    }

    private void saveFilteredData(String ruleName,String dataName, List<SavedData> dataList) {
        String tableName = "filtered_by_" + ruleName + "_on_" + dataName;
        for (SavedData data : dataList) {
            String sql = "INSERT INTO " + tableName + " (saved_data, saved_date) VALUES (?, ?)";
            jdbcTemplate.update(sql, data.getSaved_data(), data.getSaved_date());
        }
    }
}