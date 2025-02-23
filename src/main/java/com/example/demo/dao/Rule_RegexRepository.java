package com.example.demo.dao;
import com.example.demo.model.Rule_Regex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Rule_RegexRepository extends JpaRepository<Rule_Regex, Long> {
    Optional<Rule_Regex> findByRuleName(String ruleName);
}
