package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.exception.ConflictException;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeViewController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("employees", employeeService.search(null, null, null));
        return "employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new EmployeeRequest());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("employee") EmployeeRequest employee,
                      BindingResult bindingResult,
                      Model model) {
        if (employee.getPassword() == null || employee.getPassword().isBlank()) {
            bindingResult.rejectValue("password", "empty", "Password must not be empty");
        }

        if (!bindingResult.hasErrors()) {
            try {
                employeeService.create(employee);
                return "redirect:/employees/list";
            } catch (ConflictException e) {
                bindingResult.rejectValue("email", "duplicate", e.getMessage());
            }
        }

        model.addAttribute("departments", departmentService.findAll());
        return "employees/add";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String name,
                         @RequestParam(required = false) String department,
                         Model model) {
        model.addAttribute("employees", employeeService.search(name, department, null));
        model.addAttribute("name", name);
        model.addAttribute("department", department);
        return "employees/search";
    }
}
