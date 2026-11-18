package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentStatistic;
import com.example.employeemanagement.dto.MonthlyStatistic;
import com.example.employeemanagement.dto.StatisticsResponse;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public StatisticsService(EmployeeRepository employeeRepository,
                             DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public StatisticsResponse getStatistics() {
        long total = employeeRepository.count();
        long withoutDepartment = employeeRepository.countEmployeesWithoutDepartment();
        List<DepartmentStatistic> byDepartment = departmentRepository.countEmployeesByDepartment();

        List<MonthlyStatistic> byMonth = employeeRepository.countNewEmployeesByMonth();

        log.info("Statistics: total={}, withoutDepartment={}, departments={}, months={}",
                total, withoutDepartment, byDepartment.size(), byMonth.size());
        return new StatisticsResponse(total, withoutDepartment, byDepartment, byMonth);
    }
}
