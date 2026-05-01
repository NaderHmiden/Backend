package com.example.backend.DTOs;

import lombok.Data;

@Data
public class ProjectDTO {

    private Long id;
    private String name;
    private String type;
    private String description;
}
