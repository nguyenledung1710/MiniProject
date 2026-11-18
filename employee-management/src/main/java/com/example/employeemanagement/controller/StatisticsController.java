package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.StatisticsResponse;
import com.example.employeemanagement.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public StatisticsResponse getStatistics() {
        return statisticsService.getStatistics();
    }
}
