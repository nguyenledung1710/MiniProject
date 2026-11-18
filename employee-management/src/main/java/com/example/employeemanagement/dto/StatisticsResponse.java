package com.example.employeemanagement.dto;

import java.util.List;

public class StatisticsResponse {
    private long totalEmployees;
    private long employeesWithoutDepartment;
    private List<DepartmentStatistic> byDepartment;
    private List<MonthlyStatistic> newEmployeesByMonth;

    public StatisticsResponse(long totalEmployees, long employeesWithoutDepartment,
                              List<DepartmentStatistic> byDepartment,
                              List<MonthlyStatistic> newEmployeesByMonth) {
        this.totalEmployees = totalEmployees;
        this.employeesWithoutDepartment = employeesWithoutDepartment;
        this.byDepartment = byDepartment;
        this.newEmployeesByMonth = newEmployeesByMonth;
    }

    public long getTotalEmployees() { return totalEmployees; }
    public long getEmployeesWithoutDepartment() { return employeesWithoutDepartment; }
    public List<DepartmentStatistic> getByDepartment() { return byDepartment; }
    public List<MonthlyStatistic> getNewEmployeesByMonth() { return newEmployeesByMonth; }
}
