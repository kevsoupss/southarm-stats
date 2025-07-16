package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.services.LLMService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class LLMServiceImpl implements LLMService {

    private ClaudeService claudeService;

    public LLMServiceImpl(ClaudeService claudeService) {
        this.claudeService = claudeService;
    }

    @Override
    public String generateSQL(String sanitizedQuery, String schema) {
        String result = claudeService.askClaude(sanitizedQuery, schema).block();
        return result;

    }

    @Override
    public String generateSummary(String sanitizedQuery, String validatedSql, List<Map<String, Object>> queryResults) {
        String summary = claudeService.summarizeClaude(sanitizedQuery, validatedSql, queryResults).block();
        return summary;
    }

}
