package com.southarmsite.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryResult {

    private boolean success;
    private String generatedSQL;
    private String originalQuery;
    private String summary;
    private String errorMessage;
}
