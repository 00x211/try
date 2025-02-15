package com.example.demo.controller;
import com.example.demo.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/excel")
public class ExcelController {
    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) throws IOException {
        try{
            excelService.importExcel(file);
            return "文件导入成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "文件导入失败: " + e.getMessage();
        }
    }
}
