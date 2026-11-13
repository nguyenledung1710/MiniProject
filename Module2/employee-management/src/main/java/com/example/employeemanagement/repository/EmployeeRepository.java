package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** In-memory repository (no database yet), registered as a bean via @Repository. */
@Repository
public class EmployeeRepository {

    private final Map<Long, Employee> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(sequence.incrementAndGet());
        }
        store.put(employee.getId(), employee);
        return employee;
    }

    public List<Employee> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
