package com.example.demo.service;

import com.example.demo.dao.KeywordRepository;
import com.example.demo.model.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KeywordService {
    @Autowired
    private KeywordRepository keywordRepository;
    public Keyword addKeyword(Keyword keyword){
        return keywordRepository.save(keyword);
    }
    public void deleteKeyword(Long id){
        keywordRepository.deleteById(id);
    }
    public Keyword updateKeyword(Keyword keyword){
        return keywordRepository.save(keyword);
    }
    public List<Keyword> getAllKeywords(){
        return keywordRepository.findAll();
    }
}
