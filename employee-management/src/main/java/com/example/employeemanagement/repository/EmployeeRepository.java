package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);

    List<Employee> findByDepartmentId(Long departmentId);

    boolean existsByEmail(String email);

    boolean existsByDepartmentId(Long departmentId);
}
