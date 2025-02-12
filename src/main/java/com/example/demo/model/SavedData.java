//假设存储的数据类型具有下面的格式

package com.example.demo.model;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SavedData {
    private String saved_data; // 存储的文本数据
    private LocalDate saved_date; // 存储的时间
}
