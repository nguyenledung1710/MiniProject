package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           UtilityService utilityService,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeResponse create(EmployeeRequest request) {
        if (request.getName() == null || request.getName().isBlank()
                || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name, email and password are required");
        }
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Employee employee = new Employee();
        employee.setName(utilityService.formatName(request.getName()));
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setDepartment(findDepartment(request.getDepartmentId()));

        employee = employeeRepository.save(employee);
        employee.setEmployeeCode(utilityService.generateEmployeeCode(employee.getId()));
        return toResponse(employee);
    }

    public List<EmployeeResponse> search(String name, String departmentName, Long departmentId) {
        List<Employee> employees;
        if (name != null && !name.isBlank()) {
            employees = employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (departmentName != null && !departmentName.isBlank()) {
            employees = employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
        } else if (departmentId != null) {
            employees = employeeRepository.findByDepartmentId(departmentId);
        } else {
            employees = employeeRepository.findAll();
        }

        List<EmployeeResponse> result = new ArrayList<>();
        for (Employee employee : employees) {
            result.add(toResponse(employee));
        }
        return result;
    }

    public EmployeeResponse findById(Long id) {
        return toResponse(getEmployee(id));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getEmployee(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            employee.setName(utilityService.formatName(request.getName()));
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            boolean emailChanged = !request.getEmail().equals(employee.getEmail());
            if (emailChanged && employeeRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            employee.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getDepartmentId() != null) {
            employee.setDepartment(findDepartment(request.getDepartmentId()));
        }

        employeeRepository.save(employee);
        return toResponse(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(getEmployee(id));
    }

    private Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + id));
    }

    private Department findDepartment(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        return departmentRepository.findById(departmentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department not found: " + departmentId));
    }

    private EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        if (employee.getDepartment() != null) {
            response.setDepartmentId(employee.getDepartment().getId());
            response.setDepartmentName(employee.getDepartment().getName());
        }
        return response;
    }
}
