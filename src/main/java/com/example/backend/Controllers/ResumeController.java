package com.example.backend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest; // ADD THIS
import com.example.backend.DTOs.ResumeRequestDTO;
import com.example.backend.DTOs.ResumeResponseDTO;
import com.example.backend.Entities.Resume;
import com.example.backend.Services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;


    @PostMapping("/create")
    public ResponseEntity<?> createResume(
            HttpServletRequest request,
            @RequestBody ResumeRequestDTO dto) {

        Long userId = (Long) request.getAttribute("userId");
        ResumeResponseDTO resume = resumeService.createResume(userId, dto.getTitle());
        return ResponseEntity.status(201).body(Map.of(
                "resume", resume,
                "message", "Resume created successfully"
        ));
    }
    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<?> deleteResume(HttpServletRequest request,
                                          @PathVariable Long resumeId) {
        Long userId = (Long) request.getAttribute("userId");
        resumeService.deleteResume(userId, resumeId);
        return ResponseEntity.ok(Map.of("message", "Resume deleted successfully"));
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<?> getResume(HttpServletRequest request,
                                       @PathVariable Long resumeId) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(Map.of("resume", resumeService.getResumeById(userId, resumeId)));
    }

    @PutMapping("/update/{resumeId}")
    public ResponseEntity<?> updateResume(HttpServletRequest request,
                                          @PathVariable Long resumeId,
                                          @RequestPart("resumeData") String resumeDataJson,
                                          @RequestPart(value = "image", required = false) MultipartFile image,
                                          @RequestPart(value = "removeBackground", required = false) String removeBackground) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ResumeRequestDTO dto = mapper.readValue(resumeDataJson, ResumeRequestDTO.class);
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(Map.of(
                "resume", resumeService.updateResume(userId, resumeId, dto),
                "message", "Resume updated successfully"
        ));
    }

    @GetMapping("/public/{resumeId}")
    public ResponseEntity<?> getPublicResume(@PathVariable Long resumeId) {
        return ResponseEntity.ok(Map.of("resume", resumeService.getPublicResume(resumeId)));
    }


}