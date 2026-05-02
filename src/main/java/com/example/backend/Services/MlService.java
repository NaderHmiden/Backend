package com.example.backend.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class MlService {

    private final WebClient flaskClient = WebClient.builder()
            .baseUrl("http://localhost:5000")
            .build();

    public String analyzeResume(String resumeText) {
        return flaskClient.post()
                .uri("/analyze")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text", resumeText))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println(" Flask error: " + e.getMessage()))
                .block();
    }

    public String recommendJobs(String resumeText) {
        return flaskClient.post()
                .uri("/recommend-jobs")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text", resumeText))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println("Flask error: " + e.getMessage()))
                .block();
    }
}