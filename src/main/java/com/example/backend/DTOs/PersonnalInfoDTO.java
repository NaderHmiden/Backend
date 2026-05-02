package com.example.backend.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;  // ← add this

@Data
@NoArgsConstructor
public class PersonnalInfoDTO {

    private String image;
    private String fullName;
    private String profession;
    private String email;
    private String phone;
    private String location;
    private String linkedin;
    private String website;
}