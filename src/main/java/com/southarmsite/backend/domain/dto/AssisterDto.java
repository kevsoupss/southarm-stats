package com.southarmsite.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssisterDto implements Serializable {
    private String name;
    private Long assists;
    private Long wins;
    private Long losses;
}
