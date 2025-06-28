package com.southarmsite.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchResultsDto {

    private Integer matchId;
    private String teamA;
    private String teamB;
    private Integer scoreA;
    private Integer scoreB;
    private String date;
    private String location;
    private String title;

    private List<PlayerMatchStatDto> playerStats;

}
