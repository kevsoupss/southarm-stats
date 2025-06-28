package com.southarmsite.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchPlayerDto {
    private Integer playerId;
    private String firstName;
    private String lastName;

}
