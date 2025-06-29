package com.southarmsite.backend.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WinrateDto {

    private String name;
    private Double winrate;
    private Long wins;
    private Long losses;
    private Long matches;
    private String position;

}
