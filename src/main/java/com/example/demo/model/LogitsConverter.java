package com.example.demo.model;
import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class LogitsConverter implements AttributeConverter<List<List<String>>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<List<String>> logits) {
        try {
            return objectMapper.writeValueAsString(logits);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert logits to JSON", e);
        }
    }

    @Override
    public List<List<String>> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, List.class));
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to logits", e);
        }
    }
}