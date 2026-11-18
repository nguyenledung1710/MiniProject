package com.example.employeemanagement.dto;

public class MonthlyStatistic {
    private String month;
    private long employeeCount;

    public MonthlyStatistic(Integer year, Integer month, Long employeeCount) {
        this.month = String.format("%04d-%02d", year, month);
        this.employeeCount = employeeCount;
    }

    public String getMonth() { return month; }
    public long getEmployeeCount() { return employeeCount; }
}
