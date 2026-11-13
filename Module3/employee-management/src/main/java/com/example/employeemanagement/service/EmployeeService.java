package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;          // custom @Bean
    private final PasswordEncoder passwordEncoder;  // custom @Bean

    // Constructor injection (single constructor, so @Autowired is optional)
    public EmployeeService(EmployeeRepository repository,
                           UtilityService utilityService,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeResponse create(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setEmployeeCode(utilityService.generateEmployeeCode());
        employee.setFullName(utilityService.formatName(request.getFullName()));
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        return toResponse(repository.save(employee));
    }

    public List<EmployeeResponse> findAll(String keyword) {
        return repository.findAll().stream()
                .filter(e -> keyword == null || keyword.isBlank()
                        || e.getFullName().toLowerCase().contains(keyword.trim().toLowerCase()))
                .sorted(java.util.Comparator.comparing(Employee::getId))
                .map(this::toResponse)
                .toList();
    }

    public Optional<EmployeeResponse> findById(Long id) {
        return repository.findById(id).map(this::toResponse);
    }

    private EmployeeResponse toResponse(Employee employee) {
        return modelMapper.map(employee, EmployeeResponse.class);
    }
}
