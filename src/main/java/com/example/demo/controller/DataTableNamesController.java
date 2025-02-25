// 专门管理数据表的

package com.example.demo.controller;

import com.example.demo.model.DataTableNames;
import com.example.demo.service.DataTableNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/api/data_tables")
public class DataTableNamesController {

    @Autowired
    private DataTableNamesService dataTableNamesService;

    // 查找所有的表名
    @GetMapping
    public List<DataTableNames> getTableNames() {
        return dataTableNamesService.findAllTableNames();
    }

/*    // 增加
    @PostMapping
    public DataTableNames addTableNames(@RequestBody DataTableNames dataTableNames) {
        return dataTableNamesService.addTableName(dataTableNames.getTableName());
    }

    // 删除
    @DeleteMapping("/{id}")
    public void deleteTableNames(@PathVariable Long id) {
        dataTableNamesService.deleteTableNameById(id);
    }

    // 修改
    @PutMapping
    public DataTableNames updateTableNames(@RequestBody DataTableNames dataTableNames) {
        return dataTableNamesService.updateTableNameById(dataTableNames.getId(), dataTableNames.getTableName());
    }*/
}
