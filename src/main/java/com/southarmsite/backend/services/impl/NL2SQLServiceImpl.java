package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.QueryResult;
import com.southarmsite.backend.services.NL2SQLService;

import java.util.List;
import java.util.Map;

public class NL2SQLServiceImpl implements NL2SQLService {

    @Override
    public QueryResult processNaturalLanguageQuery(String naturalLanguageQuery) {
        try {
            // TODO: 1. Input validation and sanitization
            String sanitizedQuery = sanitizeInputQuery(naturalLanguageQuery);

            // TODO: 2. Generate SQL query from natural language using LLM
            String generatedSQL = llmService.generateSQL(sanitizedQuery, schemaService.getSchemaAsString());

            // TODO: 3. Sanitize and validate generated SQL (allow only SELECT)
            String validatedSQL = validateAndSanitizeSQL(generatedSQL);

            // TODO: 4. Execute SQL query using safe methods
            List<Map<String, Object>> queryResults = queryExecutionService.executeQuery(validatedSQL);

            // TODO: 5. Generate LLM summary from SQL + results using LLM
            String summary = llmService.generateSummary(sanitizedQuery, validatedSQL, queryResults);

            // TODO: 6. Build and return QueryResult
            return QueryResult.builder()
                    .originalQuery(naturalLanguageQuery)
                    .generatedSQL(validatedSQL)
                    .summary(summary)
                    .success(true)
                    .build();

        } catch (Exception e) {
            return QueryResult.error("Error " + e.getMessage());
        }
    }

    public String sanitizeInputQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }

        return query.trim().replaceAll("\\s+", " ");
    }
}
