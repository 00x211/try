package com.example.demo.dao;
import com.example.demo.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long>{
    // 查照某个类别下的关键词
    List<Keyword> findByCategoryId(Long categoryId);

    // 根据 category_id 删除关键词
    void deleteByCategoryId(Long categoryId);
}
