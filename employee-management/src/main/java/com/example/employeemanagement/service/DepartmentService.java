package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentRequest;
import com.example.employeemanagement.dto.DepartmentResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository,
                             ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public DepartmentResponse create(DepartmentRequest request) {
        checkName(request);
        if (departmentRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department already exists");
        }
        Department department = new Department();
        department.setName(request.getName());
        return toResponse(departmentRepository.save(department));
    }

    public List<DepartmentResponse> findAll() {
        List<DepartmentResponse> result = new ArrayList<>();
        for (Department department : departmentRepository.findAll()) {
            result.add(toResponse(department));
        }
        return result;
    }

    public DepartmentResponse findById(Long id) {
        return toResponse(getDepartment(id));
    }

    public DepartmentResponse update(Long id, DepartmentRequest request) {
        checkName(request);
        Department department = getDepartment(id);
        department.setName(request.getName());
        return toResponse(departmentRepository.save(department));
    }

    public void delete(Long id) {
        Department department = getDepartment(id);
        if (employeeRepository.existsByDepartmentId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department still has employees");
        }
        departmentRepository.delete(department);
    }

    private Department getDepartment(Long id) {
        return departmentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found: " + id));
    }

    private void checkName(DepartmentRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
    }

    private DepartmentResponse toResponse(Department department) {
        return modelMapper.map(department, DepartmentResponse.class);
    }
}
