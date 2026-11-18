package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.AuthResponse;
import com.example.employeemanagement.dto.LoginRequest;
import com.example.employeemanagement.dto.RegisterRequest;
import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.exception.ConflictException;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Register failed, username already exists: {}", request.getUsername());
            throw new ConflictException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        log.info("Registered new user '{}' with role {}", user.getUsername(), user.getRole());
        return new AuthResponse(jwtService.generateToken(user.getUsername(), user.getRole().name()),
                user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Login failed for user '{}': wrong username or password", request.getUsername());
            throw e;
        }

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        log.info("User '{}' logged in", user.getUsername());
        return new AuthResponse(jwtService.generateToken(user.getUsername(), user.getRole().name()),
                user.getUsername(), user.getRole().name());
    }
}
