package com.example.backend.DTOs;



import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
}