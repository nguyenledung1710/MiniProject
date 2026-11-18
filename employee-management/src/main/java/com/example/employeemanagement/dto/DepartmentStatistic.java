package com.example.employeemanagement.dto;

public class DepartmentStatistic {
    private String departmentName;
    private long employeeCount;

    public DepartmentStatistic(String departmentName, long employeeCount) {
        this.departmentName = departmentName;
        this.employeeCount = employeeCount;
    }

    public String getDepartmentName() { return departmentName; }
    public long getEmployeeCount() { return employeeCount; }
}
