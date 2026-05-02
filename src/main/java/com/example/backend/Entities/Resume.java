package com.example.backend.Entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;


import java.util.List;
import java.time.LocalDateTime;


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
    @JoinColumn(name = "userId")
    private User user;

    private String title = "Untitled Resume";

    private Boolean isPublic;

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


    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Experience> experience;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Project> project;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Education> education;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
