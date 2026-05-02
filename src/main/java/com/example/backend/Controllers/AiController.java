package com.example.backend.Controllers;
import java.util.Map;
import com.example.backend.Services.AiService;
import com.example.backend.Services.MlService;
import com.example.backend.Services.PdfService;
import com.example.backend.Services.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import com.example.backend.Services.ResumeService;
import com.example.backend.DTOs.ResumeResponseDTO;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final PdfService pdfService;
    private final MlService mlService;
    private final ResumeService resumeService;


    // 📄 Upload PDF → extract text → AI
    @PostMapping("/upload-resume")
    public ResponseEntity<?> uploadResume(
            HttpServletRequest request,
            @RequestParam MultipartFile file,@RequestParam String title) {
        try {
            System.out.println("🔐 Auth header: " + request.getHeader("Authorization"));
            Long userId = (Long) request.getAttribute("userId");

            String resumeText = pdfService.extractTextFromPdf(file);


            String aiResponse = aiService.extractResumeData(resumeText);

            String mlResponse = mlService.analyzeResume(resumeText);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode aiNode = (ObjectNode) mapper.readTree(
                    aiResponse.replaceAll("```json|```", "").trim()
            );
            JsonNode mlNode = mapper.readTree(mlResponse);
            aiNode.set("ml_analysis", mlNode.get("ml_analysis"));

            ResumeResponseDTO saved = resumeService.createResumeFromAi(userId, title, aiNode);
            return ResponseEntity.status(201).body(saved);


        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ✨ Improve summary
    @PostMapping("/enhance-summary")
    public ResponseEntity<?> enhanceSummary(@RequestBody Map<String, String> body) {
        String text = body.get("text");

        return ResponseEntity.ok(aiService.enhanceSummary(text));
    }

    // ✨ Improve job description
    @PostMapping("/enhance-job")
    public ResponseEntity<?> enhanceJob(@RequestBody Map<String, String> body) {
        String text = body.get("text");
        return ResponseEntity.ok(aiService.enhanceJobDescription(text));
    }
    // 💼 Recommend Jobs by Resume ID
    @GetMapping("/recommend-jobs/{resumeId}")
    public ResponseEntity<?> recommendJobs(
            HttpServletRequest request,
            @PathVariable Long resumeId) {
        try {
            Long userId = (Long) request.getAttribute("userId");

            // Get resume from DB
            ResumeResponseDTO resume = resumeService.getResumeById(userId, resumeId);

            // Build text from resume data
            StringBuilder resumeText = new StringBuilder();

            if (resume.getPersonalInfo() != null) {
                resumeText.append(resume.getPersonalInfo().getFullName()).append(" ");
                resumeText.append(resume.getPersonalInfo().getProfession()).append(" ");
            }

            if (resume.getProfessionalSummary() != null) {
                resumeText.append(resume.getProfessionalSummary()).append(" ");
            }

            if (resume.getSkills() != null) {
                resumeText.append(String.join(" ", resume.getSkills())).append(" ");
            }

            if (resume.getExperiences() != null) {
                resume.getExperiences().forEach(exp -> {
                    resumeText.append(exp.getPosition()).append(" ");
                    resumeText.append(exp.getCompany()).append(" ");
                    resumeText.append(exp.getDescription()).append(" ");
                });
            }

            if (resume.getEducations() != null) {
                resume.getEducations().forEach(edu -> {
                    resumeText.append(edu.getDegree()).append(" ");
                    resumeText.append(edu.getField()).append(" ");
                    resumeText.append(edu.getInstitution()).append(" ");
                });
            }

            if (resume.getProjects() != null) {
                resume.getProjects().forEach(proj -> {
                    resumeText.append(proj.getName()).append(" ");
                    resumeText.append(proj.getDescription()).append(" ");
                });
            }

            if (resume.getExperiences() != null) {
                resume.getExperiences().forEach(exp -> {
                    resumeText.append(exp.getPosition()).append(" ");
                    resumeText.append(exp.getCompany()).append(" ");
                    resumeText.append(exp.getDescription()).append(" ");
                    // repeat position to give it more weight
                    resumeText.append(exp.getPosition()).append(" ");
                    resumeText.append(exp.getPosition()).append(" ");
                });
            }

// repeat skills multiple times to boost weight
            if (resume.getSkills() != null) {
                for (int i = 0; i < 3; i++) {
                    resumeText.append(String.join(" ", resume.getSkills())).append(" ");
                }
            }

// repeat profession multiple times
            if (resume.getPersonalInfo() != null) {
                for (int i = 0; i < 5; i++) {
                    resumeText.append(resume.getPersonalInfo().getProfession()).append(" ");
                }
            }

            // Send to Flask ML model
            String mlResponse = mlService.recommendJobs(resumeText.toString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode result = mapper.readTree(mlResponse);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}