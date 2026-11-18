package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Role;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository,
                                                PasswordEncoder passwordEncoder,
                                                @Value("${app.admin.username}") String username,
                                                @Value("${app.admin.password}") String password) {
        return args -> {
            if (!userRepository.existsByUsername(username)) {
                User admin = new User();
                admin.setUsername(username);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                log.info("Created default admin account '{}'", username);
            }
        };
    }
}
