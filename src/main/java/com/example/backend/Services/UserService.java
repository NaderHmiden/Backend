package com.example.backend.Services;

import com.example.backend.DTOs.AuthResponse;
import com.example.backend.DTOs.LoginRequest;
import com.example.backend.DTOs.UserDTO;
import com.example.backend.Entities.User;
import com.example.backend.Repositories.ResumeRepo;
import com.example.backend.Repositories.UserRepo;
import com.example.backend.security.JwtUtil;

import com.example.backend.DTOs.RegisterRequest;
import com.example.backend.Entities.Resume;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepository;
    private final ResumeRepo resumeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // POST /api/users/register
    public ResponseEntity<?> register(RegisterRequest request) {
        // Check missing fields
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing required fields"));
        }

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already exists"));
        }

        // Create new user
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(newUser);

        String token = jwtUtil.generateToken(newUser.getId());
        UserDTO userDTO = toDTO(newUser);

        return ResponseEntity.status(201)
                .body(new AuthResponse("User created successfully", token, userDTO));
    }

    // POST /api/users/login
    public ResponseEntity<?> login(LoginRequest request) {
        // Check if user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password"));
        }

        String token = jwtUtil.generateToken(user.getId());
        UserDTO userDTO = toDTO(user);

        return ResponseEntity.ok(new AuthResponse("Login successfully", token, userDTO));
    }

    // GET /api/users/data
    public ResponseEntity<?> getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        return ResponseEntity.ok(Map.of("user", toDTO(user)));
    }

    // GET /api/users/resumes
    public ResponseEntity<?> getUserResumes(Long userId) {
        List<Resume> resumes = resumeRepository.findByUserId(userId);
        List<Map<String, Object>> result = resumes.stream()
                .map(r -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", r.getId());
                    map.put("title", r.getTitle());
                    map.put("updatedAt", r.getUpdatedAt() != null ? r.getUpdatedAt().toString() : "");
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(Map.of("resumes", result));
    }

    // Helper: User → UserDTO
    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}