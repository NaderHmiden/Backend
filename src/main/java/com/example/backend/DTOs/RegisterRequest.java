package com.example.backend.DTOs;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}