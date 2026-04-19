package com.example.backend.Entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnalInfo {

    private String image = "";
    private String fullName = "";
    private String profession = "";
    private String email = "";
    private String phone = "";
    private String location = "";
    private String linkedin = "";
    private String website = "";
}
