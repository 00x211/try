package com.example.demo.dao;
import com.example.demo.model.KeywordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KeywordCategoryRepository extends JpaRepository<KeywordCategory, Long> {
    // 查所有的一级分类
    List<KeywordCategory> findByParentIsNull();

    // 自定义方法：根据 parent_id 查找子类别
    List<KeywordCategory> findByParentId(Long parentId);
}
