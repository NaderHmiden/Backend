package com.example.backend.DTOs;

import lombok.Data;

@Data
public class EducationDTO {

    private Long id;
    private String institution;
    private String degree;
    private String field;
    private String graduationDate;
}