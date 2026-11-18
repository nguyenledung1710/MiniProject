package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);

    List<Employee> findByDepartmentId(Long departmentId);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department IS NULL")
    long countEmployeesWithoutDepartment();

    boolean existsByDepartmentId(Long departmentId);
}
