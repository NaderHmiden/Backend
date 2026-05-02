package com.example.backend.Services;

import com.example.backend.DTOs.ResumeRequestDTO;
import com.example.backend.DTOs.ResumeResponseDTO;
import com.example.backend.Entities.Resume;
import com.example.backend.Entities.User;
import com.example.backend.Repositories.ResumeRepo;

import com.example.backend.Repositories.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.backend.Entities.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import com.example.backend.DTOs.*;
import org.springframework.transaction.annotation.Transactional;


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

        if (!resume.getIsPublic()) {
            throw new RuntimeException("Not public");
        }

        return mapToDTO(resume);
    }

    @Transactional
    public ResumeResponseDTO updateResume(Long userId, Long resumeId, ResumeRequestDTO dto) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        resume.setTitle(dto.getTitle());
        resume.setIsPublic(dto.isPublic());
        resume.setProfessionalSummary(dto.getProfessionalSummary());
        if (dto.getSkills() != null) {
            resume.setSkills(dto.getSkills());
        }
        if (dto.getTemplate() != null) {
            resume.setTemplate(dto.getTemplate());
        }

        if (dto.getAccentColor() != null) {
            resume.setAccentColor(dto.getAccentColor());
        }

        if (dto.getPersonalInfo() != null) {
            PersonnalInfoDTO pi = dto.getPersonalInfo();
            resume.setPersonalInfo(new PersonnalInfo(
                    pi.getImage(), pi.getFullName(), pi.getProfession(),
                    pi.getEmail(), pi.getPhone(), pi.getLocation(),
                    pi.getLinkedin(), pi.getWebsite()
            ));
        }

        if (dto.getExperiences() != null) {
            resume.getExperience().clear();
            dto.getExperiences().forEach(expDto -> {
                Experience e = new Experience();
                e.setCompany(expDto.getCompany());
                e.setPosition(expDto.getPosition());
                e.setStartDate(expDto.getStartDate());
                e.setEndDate(expDto.getEndDate());
                e.setDescription(expDto.getDescription());
                e.setCurrent(expDto.isCurrent());
                e.setResume(resume);
                resume.getExperience().add(e);
            });
        }

        if (dto.getEducations() != null) {
            resume.getEducation().clear();
            dto.getEducations().forEach(eduDto -> {
                Education ed = new Education();
                ed.setInstitution(eduDto.getInstitution());
                ed.setDegree(eduDto.getDegree());
                ed.setField(eduDto.getField());
                ed.setGraduationDate(eduDto.getGraduationDate());
                ed.setResume(resume);
                resume.getEducation().add(ed);
            });
        }

        if (dto.getProjects() != null) {
            resume.getProject().clear();
            dto.getProjects().forEach(projDto -> {
                Project p = new Project();
                p.setName(projDto.getName());
                p.setType(projDto.getType());
                p.setDescription(projDto.getDescription());
                p.setResume(resume);
                resume.getProject().add(p);
            });
        }

        return mapToDTO(resumeRepository.save(resume));
    }
    public ResumeResponseDTO createResumeFromAi(Long userId, String title, JsonNode data) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Map personal_info
        JsonNode pi = data.path("personal_info");
        PersonnalInfo personalInfo = new PersonnalInfo(
                pi.path("image").asText(""),
                pi.path("full_name").asText(""),
                pi.path("profession").asText(""),
                pi.path("email").asText(""),
                pi.path("phone").asText(""),
                pi.path("location").asText(""),
                pi.path("linkedin").asText(""),
                pi.path("website").asText("")
        );

        // ✅ Map skills
        List<String> skills = new ArrayList<>();
        data.path("skills").forEach(s -> skills.add(s.asText()));

        // ✅ Build resume
        Resume resume = Resume.builder()
                .user(user)
                .title(title)
                .isPublic(false)
                .professionalSummary(data.path("professional_summary").asText(""))
                .personalInfo(personalInfo)
                .skills(skills)
                .build();

        Resume saved = resumeRepository.save(resume);

        // ✅ Map experience
        List<Experience> experiences = new ArrayList<>();
        data.path("experience").forEach(exp -> {
            Experience e = new Experience();
            e.setCompany(exp.path("company").asText(""));
            e.setPosition(exp.path("position").asText(""));
            e.setStartDate(exp.path("start_date").asText(""));
            e.setEndDate(exp.path("end_date").asText(""));
            e.setDescription(exp.path("description").asText(""));
            e.setCurrent(exp.path("is_current").asBoolean(false));
            e.setResume(saved);
            experiences.add(e);
        });

        // ✅ Map education
        List<Education> educations = new ArrayList<>();
        data.path("education").forEach(edu -> {
            Education ed = new Education();
            ed.setInstitution(edu.path("institution").asText(""));
            ed.setDegree(edu.path("degree").asText(""));
            ed.setField(edu.path("field").asText(""));
            ed.setGraduationDate(edu.path("graduation_date").asText(""));
            ed.setResume(saved);
            educations.add(ed);
        });

        // ✅ Map projects
        List<Project> projects = new ArrayList<>();
        data.path("project").forEach(proj -> {
            Project p = new Project();
            p.setName(proj.path("name").asText(""));
            p.setType(proj.path("type").asText(""));
            p.setDescription(proj.path("description").asText(""));
            p.setResume(saved);
            projects.add(p);
        });

        saved.setExperience(experiences);
        saved.setEducation(educations);
        saved.setProject(projects);

        return mapToDTO(resumeRepository.save(saved));
    }

    private ResumeResponseDTO mapToDTO(Resume resume) {

        PersonnalInfoDTO personalInfoDTO = null;
        if (resume.getPersonalInfo() != null) {
            PersonnalInfo pi = resume.getPersonalInfo();
            personalInfoDTO = new PersonnalInfoDTO();
            personalInfoDTO.setImage(pi.getImage());
            personalInfoDTO.setFullName(pi.getFullName());
            personalInfoDTO.setProfession(pi.getProfession());
            personalInfoDTO.setEmail(pi.getEmail());
            personalInfoDTO.setPhone(pi.getPhone());
            personalInfoDTO.setLocation(pi.getLocation());
            personalInfoDTO.setLinkedin(pi.getLinkedin());
            personalInfoDTO.setWebsite(pi.getWebsite());
        }

        List<ExperienceDTO> experienceDTOs = new ArrayList<>();
        if (resume.getExperience() != null) {
            resume.getExperience().forEach(exp -> {
                ExperienceDTO dto = new ExperienceDTO();
                dto.setId(exp.getId());
                dto.setCompany(exp.getCompany());
                dto.setPosition(exp.getPosition());
                dto.setStartDate(exp.getStartDate());
                dto.setEndDate(exp.getEndDate());
                dto.setDescription(exp.getDescription());
                dto.setCurrent(exp.isCurrent());
                experienceDTOs.add(dto);
            });
        }

        List<EducationDTO> educationDTOs = new ArrayList<>();
        if (resume.getEducation() != null) {
            resume.getEducation().forEach(edu -> {
                EducationDTO dto = new EducationDTO();
                dto.setId(edu.getId());
                dto.setInstitution(edu.getInstitution());
                dto.setDegree(edu.getDegree());
                dto.setField(edu.getField());
                dto.setGraduationDate(edu.getGraduationDate());
                educationDTOs.add(dto);
            });
        }

        List<ProjectDTO> projectDTOs = new ArrayList<>();
        if (resume.getProject() != null) {
            resume.getProject().forEach(proj -> {
                ProjectDTO dto = new ProjectDTO();
                dto.setId(proj.getId());
                dto.setName(proj.getName());
                dto.setType(proj.getType());
                dto.setDescription(proj.getDescription());
                projectDTOs.add(dto);
            });
        }

        return ResumeResponseDTO.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .isPublic(resume.getIsPublic())
                .professionalSummary(resume.getProfessionalSummary())
                .skills(resume.getSkills())
                .personalInfo(personalInfoDTO)
                .experiences(experienceDTOs)
                .educations(educationDTOs)
                .projects(projectDTOs)
                .build();
    }
}