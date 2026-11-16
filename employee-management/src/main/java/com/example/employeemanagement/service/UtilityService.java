package com.example.employeemanagement.service;

import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    public String generateEmployeeCode(Long id) {
        return String.format("NV%04d", id);
    }

    public String formatName(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw;
        }
        StringBuilder sb = new StringBuilder();
        for (String word : raw.trim().split("\\s+")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }
}
