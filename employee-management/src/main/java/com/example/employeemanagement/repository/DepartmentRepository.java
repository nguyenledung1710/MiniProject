package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.DepartmentStatistic;
import com.example.employeemanagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);

    @Query("SELECT new com.example.employeemanagement.dto.DepartmentStatistic(d.name, COUNT(e)) "
            + "FROM Department d LEFT JOIN d.employees e "
            + "GROUP BY d.id, d.name "
            + "ORDER BY COUNT(e) DESC, d.name")
    List<DepartmentStatistic> countEmployeesByDepartment();
}
