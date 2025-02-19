package com.example.demo.controller;
//import com.example.demo.model.Rule;
//import com.example.demo.service.RuleService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rules")
//public class RuleController {
//    @Autowired
//    private RuleService ruleService;
//
//    @PostMapping
//    public Rule addRule(@RequestBody Rule rule){
//        return ruleService.addRule(rule);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteRule(@PathVariable Long id){
//        ruleService.deleteRule(id);
//    }
//
//    @PutMapping
//    public Rule updateRule(@RequestBody Rule rule){
//        return ruleService.updateRule(rule);
//    }
//
//    @GetMapping
//    public List<Rule> getAllRules(){
//        return ruleService.getAllRules();
//    }
//}
import com.example.demo.model.Rule;
import com.example.demo.model.SavedData;
import com.example.demo.service.RuleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.io.IOException;

@Setter
@Getter
class ApplyRuleRequest {
    // Getter 和 Setter
    private String dataName;
    private String ruleName;

}

@RestController
@CrossOrigin
@RequestMapping("/api/rules")
public class RuleController {
    @Autowired
    private RuleService ruleService;

    @PostMapping
    public Rule addRule(@RequestBody Rule rule) throws IOException{
        return ruleService.addRule(rule);
    }

    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
    }

    @PutMapping
    public Rule updateRule(@RequestBody Rule rule) {
        return ruleService.updateRule(rule);
    }

    @GetMapping
    public List<Rule> getAllRules() {
        return ruleService.getAllRules();
    }

    @GetMapping("/{ruleName}")
    public Rule getRuleByName(@PathVariable String ruleName) {
        return ruleService.getRuleByName(ruleName);
    }

    @PostMapping("/apply")
    public List<SavedData> applyRule(@RequestBody ApplyRuleRequest ruleRequest) {
        return ruleService.applyRule(ruleRequest.getDataName(), ruleRequest.getRuleName());
    }
}