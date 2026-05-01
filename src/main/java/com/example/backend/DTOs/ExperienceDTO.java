package com.example.backend.DTOs;

import lombok.Data;

@Data
public class ExperienceDTO {

    private Long id;
    private String company;
    private String position;
    private String startDate;
    private String endDate;
    private String description;
    private boolean isCurrent;
}