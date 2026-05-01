package com.example.backend.Controllers;

import com.example.backend.DTOs.LoginRequest;
import com.example.backend.DTOs.RegisterRequest;
import com.example.backend.Services.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    // POST /api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    // GET /api/users/data (protected)
    @GetMapping("/data")
    public ResponseEntity<?> getUserById(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserById(userId);
    }

    // GET /api/users/resumes (protected)
    @GetMapping("/resumes")
    public ResponseEntity<?> getUserResumes(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserResumes(userId);
    }
}