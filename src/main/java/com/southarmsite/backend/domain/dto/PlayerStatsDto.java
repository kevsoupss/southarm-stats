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
public class PlayerStatsDto {

        private Integer playerId;
        private String firstName;
        private String lastName;
        private List<String> positions;
        private String photoUrl;
        private Long wins;
        private Long losses;
        private Long goals;
        private Long assists;
        private Long matches;
        private Long potm;
        private Long dotm;

}
