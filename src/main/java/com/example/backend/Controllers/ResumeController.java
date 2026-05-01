package com.example.backend.Controllers;

im
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
    public ResponseEntity<ResumeResponseDTO> createResume(@RequestParam Long userId,
                                          @RequestBody String title) {
        ResumeResponseDTO resume = resumeService.createResume(userId, title);
        return ResponseEntity.status(201)
                .body(ResumeResponseDTO.builder().build());
    }


    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<?> deleteResume(@RequestParam Long userId,
                                          @PathVariable Long resumeId) {
        resumeService.deleteResume(userId, resumeId);
        return ResponseEntity.ok("Resume deleted successfully");
    }


    @GetMapping("/{resumeId}")
    public ResponseEntity<?> getResume(@RequestParam Long userId,
                                       @PathVariable Long resumeId) {
        return ResponseEntity.ok(
                resumeService.getResumeById(userId, resumeId)
        );
    }


    @GetMapping("/public/{resumeId}")
    public ResponseEntity<?> getPublicResume(@PathVariable Long resumeId) {
        return ResponseEntity.ok(
                resumeService.getPublicResume(resumeId)
        );
    }


    @PutMapping("/update/{resumeId}")
    public ResponseEntity<?> updateResume(@RequestParam Long userId,
                                          @PathVariable Long resumeId,
                                          @RequestBody ResumeRequestDTO dto) {

        ResumeResponseDTO updated =
                resumeService.updateResume(userId, resumeId, dto);

        return ResponseEntity.ok(updated);
    }
}