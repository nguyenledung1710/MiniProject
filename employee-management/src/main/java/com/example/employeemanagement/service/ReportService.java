package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeReportResponse;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final EmployeeRepository employeeRepository;

    public ReportService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable("employeeReport")
    public EmployeeReportResponse getEmployeeReport() {
        log.info("Counting employees from database (cache miss)");
        return new EmployeeReportResponse(employeeRepository.count(), LocalDateTime.now());
    }
}
