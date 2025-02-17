package com.example.demo.controller;
import com.example.demo.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/excel")
//public class ExcelController {
//    @Autowired
//    private ExcelService excelService;
//
//    @PostMapping("/upload")
//    public String uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
//        try{
//            excelService.importExcel(file);
//            return "文件导入成功！";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "文件导入失败: " + e.getMessage();
//        }
//    }
//}
public class ExcelController {
    @Autowired
    private ExcelService excelService;

    /**
     * 上传 Excel 文件并导入数据
     *
     * @param file      上传的 Excel 文件
     * @param tableName 表名称（可选）
     * @return 导入结果
     */
    @PostMapping("/upload")
    public Map<String, String> uploadExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "tableName", required = false) String tableName) {
        try {
            return excelService.importExcel(file, tableName);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of("result", "failed", "file_name", tableName != null ? tableName : "N/A");
        }
    }
}