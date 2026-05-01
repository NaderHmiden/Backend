package com.example.backend.Repositories;

import com.example.backend.Entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EducationRepo extends JpaRepository<Education, Long> {


    List<Education> findByResumeId(Long resumeId);
}