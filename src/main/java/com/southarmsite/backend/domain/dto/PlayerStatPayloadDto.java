package com.southarmsite.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerStatPayloadDto {
    private String firstName;
    private String lastName;
    private String team;
    private Integer goals;
    private Integer assists;
    private Boolean win;
    private Boolean potm;
    private Boolean dotm;
}
