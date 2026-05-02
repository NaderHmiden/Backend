package com.example.backend.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final WebClient webClient;
    private final String model;

    public AiService(
            @Value("${gemini.base.url}") String baseUrl,
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.model}") String model
    ) {
        this.model = model;

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    // Improve Summary

    public String enhanceSummary(String userContent) {

        String systemPrompt = """
                You are an expert resume writer.
                Improve this summary in 1–2 ATS-friendly sentences.
                Return ONLY text.
                """;

        return callAI(systemPrompt, userContent);
    }

    // 2. Improve Job Description
    public String enhanceJobDescription(String userContent) {

        String systemPrompt = """
                You are an expert resume writer.
                Improve job descriptions using action verbs and measurable results.
                Return ONLY text.
                """;

        return callAI(systemPrompt, userContent);
    }

    // Extract Resume (JSON)
    public String extractResumeData(String resumeText) {

        String systemPrompt = """
            You are an expert AI Agent to extract data from a resume.
            Return ONLY valid JSON with no additional text before or after.
            """;

        String userPrompt = """
            Extract data from this resume: %s

            Return ONLY this JSON structure:
            {
              "professional_summary": "",
              "skills": [],
              "personal_info": {
                "image": "",
                "full_name": "",
                "profession": "",
                "email": "",
                "phone": "",
                "location": "",
                "linkedin": "",
                "website": ""
              },
              "experience": [
                {
                  "company": "",
                  "position": "",
                  "start_date": "",
                  "end_date": "",
                  "description": "",
                  "is_current": ""
                }
              ],
              "project": [
                {
                  "name": "",
                  "type": "",
                  "description": ""
                }
              ],
              "education": [
                {
                  "institution": "",
                  "degree": "",
                  "field": "",
                  "graduation_date": ""
                }
              ]
            }
            """.formatted(resumeText);

        return callAI(systemPrompt, userPrompt);
    }
    //  AI CALL (REUSABLE)
    private String callAI(String systemPrompt, String userContent) {
        String rawResponse = webClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", model,
                        "messages", new Object[]{
                                Map.of("role", "system", "content", systemPrompt),
                                Map.of("role", "user", "content", userContent)
                        }
                ))
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Gemini error: " + body))
                )
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            return rawResponse; // fallback to raw if parsing fails
        }
    }}