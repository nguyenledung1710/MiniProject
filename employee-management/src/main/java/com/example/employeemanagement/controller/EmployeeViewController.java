package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.exception.ConflictException;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.StatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final StatisticsService statisticsService;

    public EmployeeViewController(EmployeeService employeeService,
                                  DepartmentService departmentService,
                                  StatisticsService statisticsService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.statisticsService = statisticsService;
    }

    @InitBinder
    public void trimEmptyStrings(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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
                      Model model,
                      RedirectAttributes redirectAttributes) {
        if (employee.getPassword() == null) {
            bindingResult.rejectValue("password", "empty", "Password must not be empty");
        }

        if (!bindingResult.hasErrors()) {
            try {
                employeeService.create(employee);
                redirectAttributes.addFlashAttribute("message", "Employee added successfully");
                return "redirect:/employees/list";
            } catch (ConflictException e) {
                bindingResult.rejectValue("email", "duplicate", e.getMessage());
            }
        }

        model.addAttribute("departments", departmentService.findAll());
        return "employees/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EmployeeResponse current = employeeService.findById(id);

        EmployeeRequest form = new EmployeeRequest();
        form.setName(current.getName());
        form.setEmail(current.getEmail());
        form.setDepartmentId(current.getDepartmentId());

        model.addAttribute("employee", form);
        model.addAttribute("employeeId", id);
        model.addAttribute("employeeCode", current.getEmployeeCode());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("employee") EmployeeRequest employee,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                employeeService.update(id, employee);
                redirectAttributes.addFlashAttribute("message", "Employee updated successfully");
                return "redirect:/employees/list";
            } catch (ConflictException e) {
                bindingResult.rejectValue("email", "duplicate", e.getMessage());
            }
        }

        model.addAttribute("employeeId", id);
        model.addAttribute("employeeCode", employeeService.findById(id).getEmployeeCode());
        model.addAttribute("departments", departmentService.findAll());
        return "employees/edit";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Employee deleted successfully");
        return "redirect:/employees/list";
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

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("stats", statisticsService.getStatistics());
        return "employees/statistics";
    }
}
