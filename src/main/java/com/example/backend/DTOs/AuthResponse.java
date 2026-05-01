package com.example.backend.DTOs;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private UserDTO user;
}