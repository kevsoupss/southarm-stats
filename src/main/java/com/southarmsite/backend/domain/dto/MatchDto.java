package com.southarmsite.backend.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchDto {

    private Integer matchId;

    private LocalDate date;

    private String location;

    private String description;


}
