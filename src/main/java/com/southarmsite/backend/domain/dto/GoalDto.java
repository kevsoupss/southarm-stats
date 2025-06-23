package com.southarmsite.backend.domain.dto;

import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalDto {

    private Integer goalId;

    private MatchEntity matchEntity;

    private PlayerEntity scorer;

    private PlayerEntity assister;
}
