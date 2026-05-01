package com.example.backend.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AiService {

    private final WebClient webClient;
    private final String model;

    public AiService(
            @Value("${GEMINI_BASE_URL}") String baseUrl,
            @Value("${GEMINI_API_KEY}") String apiKey,
            @Value("${GEMINI_MODEL}") String model
    ) {
        this.model = model;

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    // =========================================
    // ✨ 1. Improve Summary
    // =========================================
    public String enhanceSummary(String userContent) {

        String systemPrompt = """
                You are an expert resume writer.
                Improve this summary in 1–2 ATS-friendly sentences.
                Return ONLY text.
                """;

        return callAI(systemPrompt, userContent);
    }

    // =========================================
    // ✨ 2. Improve Job Description
    // =========================================
    public String enhanceJobDescription(String userContent) {

        String systemPrompt = """
                You are an expert resume writer.
                Improve job descriptions using action verbs and measurable results.
                Return ONLY text.
                """;

        return callAI(systemPrompt, userContent);
    }

    // =========================================
    // 📄 3. Extract Resume (JSON)
    // =========================================
    public String extractResumeData(String resumeText) {

        String systemPrompt = """
                You are an AI that extracts structured resume data.

                Return ONLY valid JSON:

                {
                  "professional_summary": "",
                  "skills": [],
                  "personal_info": {
                    "full_name": "",
                    "email": "",
                    "phone": ""
                  },
                  "experience": [
                    {
                      "company": "",
                      "position": "",
                      "description": ""
                    }
                  ],
                  "projects": [
                    {
                      "name": "",
                      "description": ""
                    }
                  ]
                }
                """;

        return callAI(systemPrompt, resumeText);
    }

    // =========================================
    // ⚙️ CORE AI CALL (REUSABLE)
    // =========================================
    private String callAI(String systemPrompt, String userContent) {

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", model,
                        "messages", new Object[]{
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", userContent)
                        }
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}