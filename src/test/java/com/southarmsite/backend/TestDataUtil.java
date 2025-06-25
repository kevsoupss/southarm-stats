package com.southarmsite.backend;


import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;

import java.time.LocalDate;
import java.util.List;

public final class TestDataUtil {
    private TestDataUtil() {
        throw new UnsupportedOperationException("Utility classes cannot be constructed");
    }

    public static PlayerEntity createTestPlayerA() {
        return PlayerEntity.builder()
                .firstName("Kevin")
                .lastName("Lei")
                .position("Winger")
                .photoUrl("testUrl")
                .build();
    }

    public static PlayerEntity createTestPlayerB() {
        return PlayerEntity.builder()
                .firstName("Ronald")
                .lastName("Lam")
                .position("Winger")
                .photoUrl("testUrl2")
                .build();
    }

    public static PlayerEntity createTestPlayerC() {
        return PlayerEntity.builder()
                .firstName("Rickey")
                .lastName("Lam")
                .position("Striker")
                .photoUrl("testUrl3")
                .build();
    }


    public static PlayerEntity createTestPlayerD() {
        return PlayerEntity.builder()
                .firstName("Michael")
                .lastName("Jeff")
                .position("Center Midfield")
                .photoUrl("testUrl4")
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

    public static MatchEntity createTestMatchA(TeamEntity teamA, TeamEntity teamB) {
        return MatchEntity.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .teamA(teamA)
                .teamB(teamB)
                .scoreA(2)
                .scoreB(3)
                .build();
    }

    public static MatchEntity createTestMatchB(TeamEntity teamA, TeamEntity teamB) {
        return MatchEntity.builder()
                .date(LocalDate.of(2025, 5, 27))
                .location("King George")
                .teamA(teamA)
                .teamB(teamB)
                .scoreA(2)
                .scoreB(3)
                .build();
    }

    public static MatchDto createTestMatchDtoA() {
        return MatchDto.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .description("Jeff v Michael Special")
                .teamA("Team Jeff")
                .teamB("Team Michael")
                .scoreA(5)
                .scoreB(3)
                .build();
    }


    public static TeamEntity createTestTeamA(PlayerEntity captain) {
        return TeamEntity.builder()
                .name("Team Kevin")
                .captain(captain)
                .build();
    }
    public static TeamEntity createTestTeamB(PlayerEntity captain) {
        return TeamEntity.builder()
                .name("Team Ronald")
                .captain(captain)
                .build();
    }


    public static PlayerMatchStatEntity createTestPlayerMatchStatA(final MatchEntity matchEntity, final PlayerEntity playerEntity, final TeamEntity teamEntity) {
        return PlayerMatchStatEntity.builder()
                .player(playerEntity)
                .match(matchEntity)
                .team(teamEntity)
                .goals(1)
                .assists(1)
                .won(false)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatB(final MatchEntity matchEntity, final PlayerEntity playerEntity, final TeamEntity teamEntity) {
        return PlayerMatchStatEntity.builder()
                .player(playerEntity)
                .match(matchEntity)
                .team(teamEntity)
                .goals(3)
                .assists(2)
                .won(false)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatC(final MatchEntity matchEntity, final PlayerEntity playerEntity, final TeamEntity teamEntity) {
        return PlayerMatchStatEntity.builder()
                .player(playerEntity)
                .match(matchEntity)
                .team(teamEntity)
                .goals(3)
                .assists(5)
                .won(true)
                .build();
    }


}
