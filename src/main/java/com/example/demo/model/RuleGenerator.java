package com.example.demo.model;

import java.time.LocalDate;
import java.util.List;

public class RuleGenerator {
    public static String generateDrl(Rule rule) {
        StringBuilder drlBuilder = new StringBuilder();
        drlBuilder.append("package rules_drl\n\n");
        drlBuilder.append("import java.time.LocalDate;\n");
        drlBuilder.append("import com.example.demo.model.SavedData;\n\n");
        drlBuilder.append("global java.util.List<SavedData> result\n\n");
        drlBuilder.append("rule \"");
        drlBuilder.append(rule.getDescription());
        drlBuilder.append("\"\n");
        drlBuilder.append("    when\n");

        // 构建规则条件
        List<List<String>> and_logits = rule.getDataFilterLogitsAnd();
        List<List<String>> not_logits = rule.getDataFilterLogitsNot();
        LocalDate start = rule.getDataFilterLogitsTimestart();
        LocalDate end = rule.getDataFilterLogitsTimeend();
        if(and_logits==null && not_logits==null && start == null && end == null) {
            System.out.print("没有规则，创建失败");
            return "没有规则内容，创建失败。";
        }

        // 检查最后一行是否以逗号结尾
        String currentContent = drlBuilder.toString();
        int lastNewLineIndex = currentContent.lastIndexOf('\n');
        if (lastNewLineIndex != -1 && currentContent.charAt(lastNewLineIndex - 1) == ',') {
            // 删除最后一个逗号
            drlBuilder.deleteCharAt(lastNewLineIndex - 1);
        }

        drlBuilder.append("        $data : SavedData(\n");
        // 下面是常规的字符串添加方案，没有使用正则表达
//        if(!and_logits.isEmpty()) {
//            for (int i = 0; i < and_logits.size(); i++) {
//                List<String> group = and_logits.get(i);
//                drlBuilder.append("            (");
//                for(int j = 0; j < and_logits.get(i).size(); j++) {
//                    if("或".equals(group.get(j))){
//                        drlBuilder.append(" || ");
//                    }
//                    else{
//                        drlBuilder.append("saved_data contains \"").append(group.get(j)).append("\" ");
//                    }
//                }
//                drlBuilder.append(") && \n");
//            }
//        }

        // 使用正则表达式
        if(and_logits!=null) {
            for(int i=0; i<and_logits.size(); i++) {
                List<String> and_logit = and_logits.get(i);
                drlBuilder.append("            saved_data matches \"(?s).*(");
                for(int j=0; j<and_logit.size(); j++) {
                    if("或".equals(and_logit.get(j))) {
                        drlBuilder.append("|");
                    }
                    else{
                        drlBuilder.append(and_logit.get(j));
                    }
                }
                drlBuilder.append(").*\",\n");
            }
        }
        if(not_logits!=null) {
            for(int i=0; i<not_logits.size(); i++) {
                List<String> not_logit = not_logits.get(i);
                drlBuilder.append("            saved_data not matches \"(?s).*(");
                for(int j=0; j<not_logit.size(); j++) {
                    if("或".equals(not_logit.get(j))) {
                        drlBuilder.append("|");
                    }
                    else{
                        drlBuilder.append(not_logit.get(j));
                    }
                }
                drlBuilder.append(").*\",\n");
            }
        }
        if(start != null){
            drlBuilder.append(String.format("            saved_date >= LocalDate.of(%d, %d, %d),\n", start.getYear(), start.getMonthValue(), start.getDayOfMonth()));
        }
        if(end != null){
            drlBuilder.append(String.format("            saved_date >= LocalDate.of(%d, %d, %d)\n", end.getYear(), end.getMonthValue(), end.getDayOfMonth()));
        }

        drlBuilder.append("        )\n");
        drlBuilder.append("    then\n");
        drlBuilder.append("        System.out.println(\"匹配到数据: \" + $data.getSaved_data());\n");
        drlBuilder.append("        result.add($data);\n");
        drlBuilder.append("end\n");

        return drlBuilder.toString();
    }
}
