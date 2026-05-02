package com.example.backend.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResumeResponseDTO {

    private Long id;

    private String title;

    private boolean isPublic;

    private String template;

    private String accentColor;

    private String professionalSummary;

    private List<String> skills;

    private PersonnalInfoDTO personalInfo;

    private List<ExperienceDTO> experiences;

    private List<ProjectDTO> projects;

    private List<EducationDTO> educations;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}