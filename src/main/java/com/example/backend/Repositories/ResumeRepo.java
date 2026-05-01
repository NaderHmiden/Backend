package com.example.backend.Repositories;

import com.example.backend.Entities.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResumeRepo extends JpaRepository<Resume, Long> {


    List<Resume> findByUserId(Long userId);


    List<Resume> findByIsPublicTrue();

}