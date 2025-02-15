package com.example.demo.service;
import com.example.demo.dao.SavedDataRepository;
import com.example.demo.model.SavedData;
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

@Service
public class ExcelService {
    @Autowired
    private SavedDataRepository savedDataRepository;

    public void importExcel(MultipartFile file) throws IOException {
        List<SavedData> dataList = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 第一个工作表
        Iterator<Row> rowIterator = sheet.iterator();

        // 跳过表头
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            SavedData data = new SavedData();

            data.setSaved_data(row.getCell(0).getStringCellValue());
            data.setSaved_date(row.getCell(1).getDateCellValue()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            dataList.add(data);
        }

        savedDataRepository.saveAll(dataList);
    }
}
