package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.RegisterRequest;
import com.example.employeemanagement.exception.ConflictException;
import com.example.employeemanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthViewController {

    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/employees/list";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterRequest user,
                           BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            try {
                authService.register(user);
                return "redirect:/login?registered";
            } catch (ConflictException e) {
                bindingResult.rejectValue("username", "duplicate", e.getMessage());
            }
        }
        return "register";
    }
}
