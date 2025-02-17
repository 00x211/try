package com.example.demo.service;
//import com.example.demo.dao.SavedDataRepository;
//import com.example.demo.model.SavedData;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//@Service
//public class ExcelService {
//    @Autowired
//    private SavedDataRepository savedDataRepository;
//
//    public void importExcel(MultipartFile file) throws IOException {
//        List<SavedData> dataList = new ArrayList<>();
//
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0); // 第一个工作表
//        Iterator<Row> rowIterator = sheet.iterator();
//
//        // 跳过表头
//        if (rowIterator.hasNext()) {
//            rowIterator.next();
//        }
//
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            SavedData data = new SavedData();
//
//            data.setSaved_data(row.getCell(0).getStringCellValue());
//            Cell dateCell = row.getCell(1);
//            if(dateCell != null){
//                data.setSaved_date(dateCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            }
//            else{
//                data.setSaved_date(LocalDate.now());
//            }
//            dataList.add(data);
//        }
//
//        savedDataRepository.saveAll(dataList);
//    }
//}
import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
public class ExcelService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 解析 Excel 文件并存储到指定表中
     *
     * @param file     上传的 Excel 文件
     * @param tableName 表名称（可选）
     * @return 导入结果
     */
    public Map<String, String> importExcel(MultipartFile file, String tableName) throws IOException {
        Map<String, String> result = new HashMap<>();

        // 如果未指定表名称，则生成默认表名称
        if (tableName == null || tableName.isEmpty()) {
            tableName = generateDefaultTableName();
        }

        // 若指定表明，添加反引号
        tableName = "`" + tableName + "`"; // 添加反引号，防止有非法字符
        System.out.print("文件名称：" + tableName + "\n");

        // 创建表
        if (!createTable(tableName)) {
            result.put("result", "failed");
            result.put("file_name", tableName);
            return result;
        }

        // 使用 Apache POI 解析 Excel 文件
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
        Iterator<Row> rowIterator = sheet.iterator();

        // 跳过表头
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        // 遍历每一行并插入数据
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String savedData = row.getCell(0).getStringCellValue(); // 第一列：saved_data
//            LocalDate savedDate;
//            Cell dateCell = row.getCell(1);
//            if(dateCell != null) {
//                savedDate = row.getCell(1).getDateCellValue() // 第二列：saved_date
//                        .toInstant()
//                        .atZone(java.time.ZoneId.systemDefault())
//                        .toLocalDate();
//            }
//            else{
//                savedDate = LocalDate.now();
//            }
            LocalDate savedDate = LocalDate.now();

            // 插入数据
            insertData(tableName, savedData, savedDate);
        }

        result.put("result", "success");
        result.put("file_name", tableName);
        System.out.print("上传文件结束\n");
        return result;
    }

    /**
     * 生成默认表名称
     *
     * @return 默认表名称
     */
    private String generateDefaultTableName() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "`data-" + date + "-file" + uuid + "`";
    }

    /**
     * 创建表
     *
     * @param tableName 表名称
     * @return 是否创建成功
     */
    private boolean createTable(String tableName) {
        try {
            String sql = "CREATE TABLE " + tableName + " (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "saved_data TEXT NOT NULL, " +
                    "saved_date DATE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入数据
     *
     * @param tableName 表名称
     * @param savedData 数据内容
     * @param savedDate 数据日期
     */
    private void insertData(String tableName, String savedData, LocalDate savedDate) {
        String sql = "INSERT INTO " + tableName + " (saved_data, saved_date) VALUES (?, ?)";
        jdbcTemplate.update(sql, savedData, savedDate);
    }
}
