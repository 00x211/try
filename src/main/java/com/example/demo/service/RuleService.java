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
import com.example.demo.model.SavedData;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    /**
     * 添加规则
     *
     * @param rule 规则对象
     * @return 保存后的规则
     */
    public Rule addRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    /**
     * 删除规则
     *
     * @param id 规则ID
     */
    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }

    /**
     * 更新规则
     *
     * @param rule 规则对象
     * @return 更新后的规则
     */
    public Rule updateRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    /**
     * 获取所有规则
     *
     * @return 规则列表
     */
    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    /**
     * 根据规则名称查找规则
     *
     * @param ruleName 规则名称
     * @return 匹配的规则
     */
    public Rule getRuleByName(String ruleName) {
        return ruleRepository.findByRuleName(ruleName)
                .orElseThrow(() -> new RuntimeException("规则不存在: " + ruleName));
    }

    public List<SavedData> applyRule(String ruleName, List<SavedData> dataList) {
        Rule rule = ruleRepository.findByRuleName(ruleName).orElseThrow(() -> new RuntimeException("规则不存在: " + ruleName));
//        if (rule == null) {
//            throw new RuntimeException("规则不存在: " + ruleName);
//        }

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/rules_drl/" + ruleName + ".drl",
                kieServices.getResources().newReaderResource(new StringReader(rule.getRuleContent())));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = kieBuilder.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());

        KieSession kieSession = kieContainer.newKieSession();
        List<SavedData> result = new ArrayList<>();
        for (SavedData data : dataList) {
            kieSession.insert(data);
        }
        kieSession.setGlobal("result", result); // 将结果存储到全局变量
        kieSession.fireAllRules();
        kieSession.dispose();

        return result;
    }
}