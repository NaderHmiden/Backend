package com.example.backend.Repositories;

import com.example.backend.Entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ExperienceRepo  extends JpaRepository<Experience, Long> {

    List<Experience> findByResumeId(Long resumeId);
}