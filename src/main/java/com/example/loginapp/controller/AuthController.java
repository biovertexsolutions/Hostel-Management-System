package com.example.loginapp.controller;

import com.example.loginapp.model.User;
import com.example.loginapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Registration API
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {

        Optional<User> existingUser = userRepository.findByPhone(user.getPhone());

        if (existingUser.isPresent()) {
            return "Phone number already registered";
        }

        // Encrypt password before saving
        String encryptedPassword = encoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return "User registered successfully";
    }

    // Login API
    @PostMapping("/login")
    public String loginUser(@RequestParam String phone, @RequestParam String password) {

        Optional<User> userOptional = userRepository.findByPhone(phone);

        if (userOptional.isEmpty()) {
            return "Invalid phone or password";
        }

        User user = userOptional.get();

        // Compare encrypted password
        if (encoder.matches(password, user.getPassword())) {
            return "Login successful - Welcome to Admin Page";
        }

        return "Invalid phone or password";
    }
}