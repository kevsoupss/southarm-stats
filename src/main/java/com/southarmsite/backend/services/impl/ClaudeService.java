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


    public ClaudeService(WebClient.Builder webClientBuilder, @Value("${claude.api.key}") String apiKey) {
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
        Please only create Select SQL statements on a strict PostgresSQL database. 
        Please output only the raw SQL query, without any markdown formatting or code fences.
        Here is the schema:
        %s
        """.formatted(schema);

        Map<String, Object> body = Map.of(
                "model", "claude-3-5-haiku-20241022",
                "max_tokens", 500,
                "system", systemPrompt,  // System prompt goes here, not in messages
                "messages", List.of(
                        Map.of("role", "user", "content", userPrompt)  // Only user messages
                )
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnNext(response -> System.out.println("Claude response raw: " + response))
                .map(response -> {
                    var content = (List<Map<String, Object>>) response.get("content");
                    return (String) content.get(0).get("text");
                })
                .doOnSuccess(text -> System.out.println("Extracted text: " + text))
                .doOnError(err -> System.err.println("Error from Claude API: " + err.getMessage()))
                .onErrorResume(error -> Mono.error(new RuntimeException("Failed to get response from Claude API", error)));
    }


    public Mono<String> summarizeClaude(String sanitizedQuery, String validatedSql, List<Map<String, Object>> queryResults) {
        String systemPrompt = """
        You are a helpful assistant that summarizes SQL queries and their results into human-friendly summaries.
        Simply return the query results with no SQL, with a small summary. No need to mention unknown data/results. 
        """;

        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append("Sanitized Query:\n").append(sanitizedQuery).append("\n\n");
        userPrompt.append("Validated SQL:\n").append(validatedSql).append("\n\n");

        if (queryResults == null || queryResults.isEmpty()) {
            userPrompt.append("Query Results: No results returned.\n");
        } else {
            userPrompt.append("Query Results:\n");
            int rowLimit = Math.min(10, queryResults.size());
            for (int i = 0; i < rowLimit; i++) {
                userPrompt.append("Row ").append(i + 1).append(": ").append(queryResults.get(i)).append("\n");
            }
        }

        Map<String, Object> body = Map.of(
                "model", "claude-3-5-haiku-20241022",
                "max_tokens", 500,
                "system", systemPrompt,  // System prompt goes here, not in messages
                "messages", List.of(
                        Map.of("role", "user", "content", userPrompt)  // Only user messages
                )
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var content = (List<Map<String, Object>>) response.get("content");
                    return (String) content.get(0).get("text");
                })
                .onErrorResume(error -> Mono.just("Claude summarization failed: " + error.getMessage()));
    }

}
