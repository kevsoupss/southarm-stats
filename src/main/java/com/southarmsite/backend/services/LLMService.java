package com.southarmsite.backend.services;

public interface LLMService {
    String generateSQL(String sanitizedQuery, String schema);
}
