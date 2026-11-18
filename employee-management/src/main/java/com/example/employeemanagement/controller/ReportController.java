package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeReportResponse;
import com.example.employeemanagement.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/employee-count")
    public EmployeeReportResponse employeeCount() {
        return reportService.getEmployeeReport();
    }
}
