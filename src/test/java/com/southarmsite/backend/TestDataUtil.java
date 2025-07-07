package com.southarmsite.backend;

import com.southarmsite.backend.domain.dto.*;
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

    // ============
    // ENTITIES
    // ============

    public static PlayerEntity createTestPlayerA() {
        return PlayerEntity.builder()
                .firstName("Kevin")
                .lastName("Lei")
                .positions(List.of("Winger"))
                .photoUrl("testUrl")
                .build();
    }

    public static PlayerEntity createTestPlayerB() {
        return PlayerEntity.builder()
                .firstName("Ronald")
                .lastName("Lam")
                .positions(List.of("Winger"))
                .photoUrl("testUrl2")
                .build();
    }

    public static PlayerEntity createTestPlayerC() {
        return PlayerEntity.builder()
                .firstName("Rickey")
                .lastName("Lam")
                .positions(List.of("Striker"))
                .photoUrl("testUrl3")
                .build();
    }

    public static PlayerEntity createTestPlayerD() {
        return PlayerEntity.builder()
                .firstName("Michael")
                .lastName("Jeff")
                .positions(List.of("Central Midfielder"))
                .photoUrl("testUrl4")
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

    public static MatchEntity createTestMatchA(TeamEntity teamA, TeamEntity teamB) {
        return MatchEntity.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .title("CLassico")
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
                .title("Jeff v Michael Classic")
                .scoreA(2)
                .scoreB(3)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatA(MatchEntity matchEntity, PlayerEntity playerEntity, TeamEntity teamEntity) {
        return PlayerMatchStatEntity.builder()
                .player(playerEntity)
                .match(matchEntity)
                .team(teamEntity)
                .goals(1)
                .assists(1)
                .won(false)
                .potm(true)
                .dotm(false)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatB(MatchEntity matchEntity, PlayerEntity playerEntity, TeamEntity teamEntity) {
        return PlayerMatchStatEntity.builder()
                .player(playerEntity)
                .match(matchEntity)
                .team(teamEntity)
                .goals(3)
                .assists(2)
                .won(false)
                .potm(true)
                .dotm(false)
                .build();
    }

    public static PlayerMatchStatEntity createTestPlayerMatchStatC(MatchEntity matchEntity, PlayerEntity playerEntity, TeamEntity teamEntity) {
        return PlayerMatchStatEntity.builder()
                .player(playerEntity)
                .match(matchEntity)
                .team(teamEntity)
                .goals(3)
                .assists(5)
                .potm(false)
                .dotm(false)
                .won(true)
                .build();
    }

    // ============
    // DTOS
    // ============

    public static PlayerDto createTestPlayerDtoA() {
        return PlayerDto.builder()
                .firstName("Kevin")
                .lastName("Lei")
                .positions(List.of("Central Midfielder"))
                .photoUrl("testUrl")
                .build();
    }

    public static PlayerDto createTestPlayerDtoB() {
        return PlayerDto.builder()
                .firstName("Ronald")
                .lastName("Lam")
                .positions(List.of("Central Midfielder"))
                .photoUrl("testUrl")
                .build();
    }

    public static TeamDto createTestTeamDtoA(MatchPlayerDto captain) {
        return TeamDto.builder()
                .name("Team Kevin")
                .captain(captain)
                .build();
    }

    public static TeamDto createTestTeamDtoB(MatchPlayerDto captain) {
        return TeamDto.builder()
                .name("Team Ronald")
                .captain(captain)
                .build();
    }

    public static MatchDto createTestMatchDtoA(TeamDto teamA, TeamDto teamB) {
        return MatchDto.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .teamA(teamA)
                .teamB(teamB)
                .title("Classico")
                .scoreA(5)
                .scoreB(3)
                .build();
    }

    public static MatchDto createTestMatchDtoB(TeamDto teamA, TeamDto teamB) {
        return MatchDto.builder()
                .date(LocalDate.of(2025, 5, 27))
                .location("Southarm")
                .teamA(teamA)
                .teamB(teamB)
                .title("Classico")
                .scoreA(5)
                .scoreB(3)
                .build();
    }

    public static PlayerMatchStatDto createTestPlayerMatchStatDtoA(Integer matchId, MatchPlayerDto matchPlayerDto, Integer teamId) {
        return PlayerMatchStatDto.builder()
                .player(matchPlayerDto)
                .goals(1)
                .assists(1)
                .won(false)
                .matchId(matchId)
                .teamId(teamId)
                .potm(true)
                .dotm(true)
                .build();
    }

    public static PlayerMatchStatDto createTestPlayerMatchStatDtoB(Integer matchId, MatchPlayerDto matchPlayerDto, Integer teamId) {
        return PlayerMatchStatDto.builder()
                .player(matchPlayerDto)
                .goals(2)
                .assists(1)
                .won(true)
                .matchId(matchId)
                .teamId(teamId)
                .potm(true)
                .dotm(true)
                .build();
    }

    public static MatchPlayerDto createMatchPlayerDto(PlayerDto playerDto) {
        return MatchPlayerDto.builder()
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .playerId(playerDto.getPlayerId())
                .build();
    }


}
