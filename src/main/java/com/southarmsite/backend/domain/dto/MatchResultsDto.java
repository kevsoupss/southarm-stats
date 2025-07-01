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
    private Integer teamAId;
    private String teamB;
    private Integer teamBId;
    private Integer scoreA;
    private Integer scoreB;
    private LocalDate date;
    private String location;
    private String title;
    private String captainA;
    private String captainB;
    private List<PlayerMatchStatDto> playersA;
    private List<PlayerMatchStatDto> playersB;
    private String potm;
    private String dotm;

}
