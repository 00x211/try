package com.example.demo.model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.List;
public class ListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            if (attribute == null) {
                return null;
            }
            // 使用 ObjectMapper 将 List<String> 转换为 JSON 字符串
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            // 若转换过程中出现异常，抛出运行时异常
            throw new RuntimeException("Error converting list to JSON string", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null;
            }
            // 使用 ObjectMapper 将 JSON 字符串转换为 List<String>
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // 若转换过程中出现异常，抛出运行时异常
            throw new RuntimeException("Error converting JSON string to list", e);
        }
    }
}
