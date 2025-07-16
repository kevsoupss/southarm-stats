package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.QueryResult;
import com.southarmsite.backend.services.LLMService;
import com.southarmsite.backend.services.NL2SQLService;
import com.southarmsite.backend.services.SchemaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NL2SQLServiceImpl implements NL2SQLService {

    private LLMService llmService;
    private SchemaService schemaService;
    private QueryExecutionService queryExecutionService;
    public NL2SQLServiceImpl(LLMService llmService, SchemaService schemaService, QueryExecutionService queryExecutionService){
        this.llmService = llmService;
        this.schemaService = schemaService;
        this.queryExecutionService = queryExecutionService;
    }

    @Override
    public QueryResult processNaturalLanguageQuery(String naturalLanguageQuery) {
        try {
            // Input validation and sanitization
            String sanitizedQuery = sanitizeInputQuery(naturalLanguageQuery);

            // Generate SQL query from natural language using LLM
            String generatedSQL = llmService.generateSQL(sanitizedQuery, schemaService.getSchemaAsString());

            // Sanitize and validate generated SQL (allow only SELECT)
            String validatedSQL = validateAndSanitizeSQL(generatedSQL);

            // Execute SQL query using safe methods
            List<Map<String, Object>> queryResults = queryExecutionService.executeQuery(validatedSQL);

            // Generate LLM summary from SQL + results using LLM
            String summary = llmService.generateSummary(sanitizedQuery, validatedSQL, queryResults);

            // Build and return QueryResult
            return QueryResult.builder()
                    .originalQuery(naturalLanguageQuery)
                    .generatedSQL(generatedSQL)
                    .summary(summary)
                    .success(true)
                    .build();

        } catch (Exception e) {
          return QueryResult.builder().errorMessage("Error " + e.getMessage()).build();
        }
    }

    public String sanitizeInputQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }

        return query.trim().replaceAll("\\s+", " ");
    }

    public String validateAndSanitizeSQL(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL string cannot be null or empty.");
        }

        String trimmed = sql.trim();

        String[] blacklist = {
                // Comment injection
                "--", "/*", "*/",

                // Data modification (just in case)
                "insert", "update", "delete", "drop", "alter", "create", "truncate",

                // Stored procedure execution
                "exec", "execute", "sp_", "xp_",

                // File operations
                "load_file", "into outfile", "into dumpfile",

                // System functions that could be misused
                "openrowset", "opendatasource", "cmdshell"
        };
        String lower = trimmed.toLowerCase();

        for (String bad : blacklist) {
            if (lower.contains(bad)) {
                throw new IllegalArgumentException("Potentially unsafe SQL content detected: '" + bad + "'");
            }
        }

        return trimmed;
    }

}
