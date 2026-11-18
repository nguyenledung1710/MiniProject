package com.example.employeemanagement.dto;

import java.time.LocalDateTime;

public class EmployeeReportResponse {
    private long totalEmployees;
    private LocalDateTime generatedAt;

    public EmployeeReportResponse(long totalEmployees, LocalDateTime generatedAt) {
        this.totalEmployees = totalEmployees;
        this.generatedAt = generatedAt;
    }

    public long getTotalEmployees() { return totalEmployees; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
}
