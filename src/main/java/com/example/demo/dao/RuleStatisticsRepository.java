package com.example.demo.dao;
import com.example.demo.model.RuleStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleStatisticsRepository extends JpaRepository<RuleStatistics, Long> {
}
