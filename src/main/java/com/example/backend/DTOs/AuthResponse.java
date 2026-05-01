package com.example.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private UserDTO user;
}