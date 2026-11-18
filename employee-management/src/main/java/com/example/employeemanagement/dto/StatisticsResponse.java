package com.example.employeemanagement.dto;

import java.util.List;

public class StatisticsResponse {
    private long totalEmployees;
    private long employeesWithoutDepartment;
    private List<DepartmentStatistic> byDepartment;

    public StatisticsResponse(long totalEmployees, long employeesWithoutDepartment,
                              List<DepartmentStatistic> byDepartment) {
        this.totalEmployees = totalEmployees;
        this.employeesWithoutDepartment = employeesWithoutDepartment;
        this.byDepartment = byDepartment;
    }

    public long getTotalEmployees() { return totalEmployees; }
    public long getEmployeesWithoutDepartment() { return employeesWithoutDepartment; }
    public List<DepartmentStatistic> getByDepartment() { return byDepartment; }
}
