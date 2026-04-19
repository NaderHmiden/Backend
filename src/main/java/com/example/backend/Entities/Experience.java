package com.example.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String position;
    private String startDate;
    private String endDate;

    @Column(length = 2000)
    private String description;

    private boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
