package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    private UtilityService utilityService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET /api/employees
     * Optional filters: ?name=an  &department=IT  &departmentId=1 (can be combined).
     */
    @GetMapping
    public List<EmployeeResponse> search(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String department,
                                         @RequestParam(required = false) Long departmentId) {
        return employeeService.search(name, department, departmentId);
    }

    @GetMapping("/{id}")
    public EmployeeResponse findById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    /** Returns 201 Created + Location header. */
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest request) {
        EmployeeResponse created = employeeService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @RequestBody EmployeeRequest request) {
        return employeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** GET /api/employees/format-name?name=nguyen van a returns "Nguyen Van A" */
    @GetMapping("/format-name")
    public String formatName(@RequestParam String name) {
        return utilityService.formatName(name);
    }
}
