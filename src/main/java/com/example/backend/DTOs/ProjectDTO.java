package com.example.backend.DTOs;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProjectDTO {

    private Long id;
    private String name;
    private String type;
    private String description;
}
