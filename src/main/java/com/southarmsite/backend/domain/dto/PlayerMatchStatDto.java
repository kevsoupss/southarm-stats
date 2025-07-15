package com.southarmsite.backend.domain.dto;

import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerMatchStatDto {

    private Integer playerMatchStatId;

    private MatchPlayerDto player;

    private Integer goals;

    private Integer assists;

    private Integer ownGoals;

    private Boolean won;

    private Integer matchId;

    private Integer teamId;

    private Boolean potm;

    private Boolean dotm;

}
