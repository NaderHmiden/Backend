package com.example.backend.Controllers;

import com.example.backend.Services.AiService;
import com.example.backend.Services.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final PdfService pdfService;

    // 📄 Upload PDF → extract text → AI
    @PostMapping("/upload-resume")
    public ResponseEntity<?> uploadResume(@RequestParam MultipartFile file) {

        String resumeText = pdfService.extractTextFromPdf(file);

        String aiResponse = aiService.extractResumeData(resumeText);

        return ResponseEntity.ok(aiResponse);
    }

    // ✨ Improve summary
    @PostMapping("/enhance-summary")
    public ResponseEntity<?> enhanceSummary(@RequestBody String text) {
        return ResponseEntity.ok(aiService.enhanceSummary(text));
    }

    // ✨ Improve job description
    @PostMapping("/enhance-job")
    public ResponseEntity<?> enhanceJob(@RequestBody String text) {
        return ResponseEntity.ok(aiService.enhanceJobDescription(text));
    }
}