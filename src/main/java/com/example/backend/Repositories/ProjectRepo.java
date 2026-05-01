package com.example.backend.Repositories;

import com.example.backend.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {


    List<Project> findByResumeId(Long resumeId);
}