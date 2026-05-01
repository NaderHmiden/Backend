package com.example.backend.Controllers;

import jakarta.servlet.http.HttpServletRequest; // ADD THIS
import com.example.backend.DTOs.ResumeRequestDTO;
import com.example.backend.DTOs.ResumeResponseDTO;
import com.example.backend.Entities.Resume;
import com.example.backend.Services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;


    @PostMapping("/create")
    public ResponseEntity<ResumeResponseDTO> createResume(
            HttpServletRequest request,
            @RequestBody ResumeRequestDTO dto) {

        Long userId = (Long) request.getAttribute("userId"); // extracted by JwtAuthFilter
        ResumeResponseDTO resume = resumeService.createResume(userId, dto.getTitle());
        return ResponseEntity.status(201).body(resume);
    }
    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<?> deleteResume(HttpServletRequest request,
                                          @PathVariable Long resumeId) {
        Long userId = (Long) request.getAttribute("userId");
        resumeService.deleteResume(userId, resumeId);
        return ResponseEntity.ok("Resume deleted successfully");
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<?> getResume(HttpServletRequest request,
                                       @PathVariable Long resumeId) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(resumeService.getResumeById(userId, resumeId));
    }

    @PutMapping("/update/{resumeId}")
    public ResponseEntity<?> updateResume(HttpServletRequest request,
                                          @PathVariable Long resumeId,
                                          @RequestBody ResumeRequestDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(resumeService.updateResume(userId, resumeId, dto));
    }


}