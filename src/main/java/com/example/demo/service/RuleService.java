package com.example.demo.service;
import com.example.demo.dao.RuleRepository;
import com.example.demo.model.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;
    public Rule addRule(Rule rule){ // 增加规则
        return ruleRepository.save(rule);
    }
    public void deleteRule(Long id){ // 使用id来删除规则（前端应该返回id）
        ruleRepository.deleteById(id);
    }
    public Rule updateRule(Rule rule){ // 修改规则 暂时使用保存新规则，应该是把旧规则修改
        return ruleRepository.save(rule);
    }
    public List<Rule> getAllRules(){ // 查找所有的规则
        return ruleRepository.findAll();
    }

}
