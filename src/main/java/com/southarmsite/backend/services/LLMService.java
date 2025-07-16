package com.southarmsite.backend.services;

import java.util.List;
import java.util.Map;

public interface LLMService {
    String generateSQL(String sanitizedQuery, String schema);

    String generateSummary(String sanitizedQuery, String validatedSql, List<Map<String, Object>> queryResults);
}
