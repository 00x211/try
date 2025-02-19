package com.example.demo.util;
import java.util.List;
public class RuleParser {
    /**
     * 把逻辑语法和关键词转化为正则表达式
     *
     * @param logic 逻辑语法 与或非
     *        keyword 关键词列表
     * @return 正则表达式
     */
    public static String toRegex(String logic, List<String> keywords) {
        StringBuilder regexBuilder = new StringBuilder();

        switch (logic.toLowerCase()) {
            case "and": // 与
                for (String keyword : keywords) {
                    regexBuilder.append("(?=.*").append(keyword).append(")");
                }
                break;
            case "or": // 或
                regexBuilder.append(String.join("|", keywords));
                break;
            case "not": // 非
                regexBuilder.append("^(?!.*(").append(String.join("|", keywords)).append("))");
                break;
            default:
                throw new IllegalArgumentException("不支持的逻辑语法: " + logic);
        }

        return regexBuilder.toString();
    }

    public static String toComplexRegex(List<String> andKeywords, List<String> orKeywords, List<String> notKeywords) {
        StringBuilder regexBuilder = new StringBuilder();

        // 与逻辑
        for (String keyword : andKeywords) {
            regexBuilder.append("(?=.*").append(keyword).append(")");
        }

        // 或逻辑
        if (!orKeywords.isEmpty()) {
            regexBuilder.append("(").append(String.join("|", orKeywords)).append(")");
        }

        // 非逻辑
        if (!notKeywords.isEmpty()) {
            regexBuilder.append("^(?!.*(").append(String.join("|", notKeywords)).append("))");
        }

        return regexBuilder.toString();
    }
}
