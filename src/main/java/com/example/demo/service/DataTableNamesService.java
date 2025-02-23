// 关于数据表的维护

package com.example.demo.service;

import com.example.demo.dao.DataTableNamesRepository;
import com.example.demo.model.DataTableNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DataTableNamesService {
    @Autowired
    private DataTableNamesRepository dataTableNamesRepository;

    public DataTableNames addTableName(String tableName) {
        DataTableNames dataTableNames = new DataTableNames();
        dataTableNames.setTableName(tableName);
        return dataTableNamesRepository.save(dataTableNames);
    }

    public List<DataTableNames> findAllTableNames() {
        return dataTableNamesRepository.findAll();
    }

    public DataTableNames findTableNameById(Long id) {
        return dataTableNamesRepository.findById(id).orElse(null);
    }

    public DataTableNames updateTableNameById(Long id, String dataTableNames) {
        DataTableNames dataTableName = dataTableNamesRepository.findById(id).orElseThrow(() -> new RuntimeException("数据表不存在,id:" + id));
        dataTableName.setTableName(dataTableNames);
        return dataTableNamesRepository.save(dataTableName);
    }

    public void deleteTableNameById(Long id) {
        dataTableNamesRepository.deleteById(id);
    }
}
