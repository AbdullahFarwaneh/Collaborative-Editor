package com.example.collabeditor.controller;

import com.example.collabeditor.dto.RegisterRequest;
import com.example.collabeditor.model.User;
import com.example.collabeditor.repository.UserRepository;
import com.example.collabeditor.Security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          JwtUtil jwtUtil,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest body) {
        if (body.getName() == null || body.getName().isBlank()
                || body.getEmail() == null || body.getEmail().isBlank()
                || body.getPassword() == null || body.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Missing name/email/password");
        }

        if (userRepository.findByEmail(body.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setName(body.getName());
        user.setEmail(body.getEmail());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<User> result = userRepository.findByEmail(email);

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = result.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        String token = jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
