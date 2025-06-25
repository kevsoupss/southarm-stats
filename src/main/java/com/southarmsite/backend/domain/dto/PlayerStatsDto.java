package com.southarmsite.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerStatsDto {

        private Integer playerId;
        private String firstName;
        private String lastName;
        private String position;
        private String photoUrl;
        private Long wins;
        private Long losses;
        private Long goals;
        private Long assists;
        private Long matches;

}
