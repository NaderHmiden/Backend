package com.example.backend.DTOs;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class EducationDTO {

    private Long id;
    private String institution;
    private String degree;
    private String field;
    private String graduationDate;
}