package com.example.backend.Entities;


import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title = "Untitled Resume";

    private boolean isPublic = false;

    private String template = "classic";

    private String accentColor = "#3B82F6";

    @Column(length = 2000)
    private String professionalSummary = "";


    @ElementCollection
    @CollectionTable(name = "resume_skills", joinColumns = @JoinColumn(name = "resume_id"))
    @Column(name = "skill")
    private List<String> skills;


    @Embedded
    private PersonnalInfo personalInfo;


    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Experience> experience;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Project> project;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Education> education;
}
