package com.example.backend.DTOs;

import lombok.Data;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeRequestDTO {

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
}