package com.example.employeemanagement.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/** Helper bean (@Service): string formatting and automatic employee code generation. */
@Service
public class UtilityService {

    private final AtomicInteger counter = new AtomicInteger();

    /** Generates codes like NV0001, NV0002, ... */
    public String generateEmployeeCode() {
        return String.format("NV%04d", counter.incrementAndGet());
    }

    /** "  nguyen   van a " becomes "Nguyen Van A" */
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
