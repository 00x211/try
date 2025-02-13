package com.example.demo.dao;
import com.example.demo.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RuleRepository extends JpaRepository<Rule, Long>{
    /**
     * 根据规则名称查找规则
     *
     * @param ruleName 规则名称
     * @return 匹配的规则
     */
    Optional<Rule> findByRuleName(String ruleName);
}
