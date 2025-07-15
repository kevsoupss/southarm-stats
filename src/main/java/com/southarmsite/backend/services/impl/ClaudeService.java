package com.southarmsite.backend.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ClaudeService {

    private final WebClient webClient;
    private String apiKey;

    public ClaudeService(WebClient.Builder webClientBuilder, @Value("${claude.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder
                .baseUrl("https://api.anthropic.com/v1/messages")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .build();
    }

    public Mono<String> askClaude(String userPrompt, String schema) {
        String systemPrompt = """
            You are a helpful assistant that turns natural language questions into SQL queries.
            Here is the schema:
            %s
            """.formatted(schema);

        Map<String, Object> body = Map.of(
                "model", "claude-sonnet-4-20250514",
                "max_tokens", 1000,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var content = (List<Map<String, Object>>) response.get("content");
                    return (String) content.get(0).get("text");
                }).onErrorResume(error -> {
                    return Mono.error(new RuntimeException("Failed to get response from Claude API", error));
                });
    }
}
