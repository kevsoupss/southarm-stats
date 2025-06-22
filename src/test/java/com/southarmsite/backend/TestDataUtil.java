package com.southarmsite.backend;


import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.GoalEntity;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;

import java.time.LocalDate;

public final class TestDataUtil {
    private TestDataUtil() {
        throw new UnsupportedOperationException("Utility classes cannot be constructed");
    }

    public static PlayerEntity createTestPlayerA() {
        return PlayerEntity.builder()
                .firstName("Kevin")
                .lastName("Lei")
                .position("Winger")
                .pictureUrl("testUrl")
                .build();
    }

    public static PlayerEntity createTestPlayerB() {
        return PlayerEntity.builder()
                .firstName("Ronald")
                .lastName("Lam")
                .position("Winger")
                .pictureUrl("testUrl2")
                .build();
    }

    public static PlayerEntity createTestPlayerC() {
        return PlayerEntity.builder()
                .firstName("Rickey")
                .lastName("Lam")
                .position("Striker")
                .pictureUrl("testUrl3")
                .build();
    }


    public static PlayerEntity createTestPlayerD() {
        return PlayerEntity.builder()
                .firstName("Michael")
                .lastName("Jeff")
                .position("Center Midfield")
                .pictureUrl("testUrl4")
                .build();
    }

    public static PlayerDto createTestPlayerDtoA() {
        return PlayerDto.builder()
                .firstName("Kevin")
                .lastName("Lei")
                .position("Winger")
                .pictureUrl("testUrl")
                .build();
    }

    public static MatchEntity createTestMatchA() {
        return MatchEntity.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .description("Jeff v Michael Special")
                .build();
    }

    public static MatchEntity createTestMatchB() {
        return MatchEntity.builder()
                .date(LocalDate.of(2025, 5, 27))
                .location("King George")
                .description("Kevin v Ronald Bday Special")
                .build();
    }

    public static MatchDto createTestMatchDtoA() {
        return MatchDto.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .description("Jeff v Michael Special")
                .build();
    }


    public static GoalEntity createTestGoal(final MatchEntity matchEntity, final PlayerEntity scorer, final PlayerEntity assister) {
        return GoalEntity.builder()
                .matchEntity(matchEntity)
                .scorer(scorer)
                .assister(assister)
                .build();
    }


    public static PlayerMatchStatEntity createTestPlayerMatchStatA(final MatchEntity matchEntity, final PlayerEntity playerEntity) {
        return PlayerMatchStatEntity.builder()
                .matchEntity(matchEntity)
                .playerEntity(playerEntity)
                .goalsScored(2)
                .assists(1)
                .captain(false)
                .win(false)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatB(final MatchEntity matchEntity, final PlayerEntity playerEntity) {
        return PlayerMatchStatEntity.builder()
                .matchEntity(matchEntity)
                .playerEntity(playerEntity)
                .goalsScored(3)
                .assists(3)
                .captain(true)
                .win(true)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatC(final MatchEntity matchEntity, final PlayerEntity playerEntity) {
        return PlayerMatchStatEntity.builder()
                .matchEntity(matchEntity)
                .playerEntity(playerEntity)
                .goalsScored(2)
                .assists(1)
                .captain(true)
                .win(true)
                .build();
    }


}
