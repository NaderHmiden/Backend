package com.example.backend.Services;

import com.example.backend.DTOs.ResumeRequestDTO;
import com.example.backend.DTOs.ResumeResponseDTO;
import com.example.backend.Entities.Resume;
import com.example.backend.Entities.User;
import com.example.backend.Repositories.ResumeRepo;

import com.example.backend.Repositories.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepo resumeRepository;
    private final UserRepo userRepository;

    // CREATE
    public ResumeResponseDTO createResume(Long userId, String title) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = Resume.builder()
                .user(user)
                .title(title)
                .isPublic(false)
                .build();

        return mapToDTO(resumeRepository.save(resume));
    }

    // DELETE
    public void deleteResume(Long userId, Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        resumeRepository.delete(resume);
    }


    public ResumeResponseDTO getResumeById(Long userId, Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        return mapToDTO(resume);
    }

    // GET PUBLIC
    public ResumeResponseDTO getPublicResume(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.isPublic()) {
            throw new RuntimeException("Not public");
        }

        return mapToDTO(resume);
    }


    public ResumeResponseDTO updateResume(Long userId, Long resumeId,
                                          ResumeRequestDTO dto) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        resume.setTitle(dto.getTitle());
        resume.setPublic(dto.isPublic());
        resume.setProfessionalSummary(dto.getProfessionalSummary());
        resume.setSkills(dto.getSkills());

        return mapToDTO(resumeRepository.save(resume));
    }


    private ResumeResponseDTO mapToDTO(Resume resume) {
        return ResumeResponseDTO.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .isPublic(resume.isPublic())
                .professionalSummary(resume.getProfessionalSummary())
                .skills(resume.getSkills())
                .build();
    }
}