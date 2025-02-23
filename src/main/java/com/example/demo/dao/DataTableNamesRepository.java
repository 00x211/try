package com.example.demo.dao;
import com.example.demo.model.DataTableNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTableNamesRepository extends JpaRepository<DataTableNames, Long> {
}
