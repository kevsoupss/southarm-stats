package com.southarmsite.backend.domain.dto;

import com.southarmsite.backend.domain.entities.PlayerEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamDto {

    private Integer teamId;

    private String name;

    private MatchPlayerDto captain;

}
