package com.southarmsite.backend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.services.MatchService;
import com.southarmsite.backend.services.PlayerMatchStatService;
import com.southarmsite.backend.services.PlayerService;
import com.southarmsite.backend.services.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.southarmsite.backend.TestDataUtil.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTest {

    private PlayerService playerService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private TeamService teamService;
    private MatchService matchService;
    private PlayerMatchStatService statService;

    @Autowired
    public PlayerControllerIntegrationTest(
            MockMvc mockMvc,
            PlayerService playerService,
            MatchService matchService,
            TeamService teamService,
            PlayerMatchStatService statService) {
        this.mockMvc = mockMvc;
        this.playerService = playerService;
        this.objectMapper = new ObjectMapper();
        this.matchService = matchService;
        this.teamService = teamService;
        this.statService = statService;
    }


    @Test
    public void testThatCreatePlayerSuccessfullyReturnsHttp201Created() throws Exception{
        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
        testPlayerDtoA.setPlayerId(null);
        String playerJson = objectMapper.writeValueAsString(testPlayerDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJson)

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatePlayerSuccessfullyReturnsSavedPlayer() throws Exception{
        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
        testPlayerDtoA.setPlayerId(null);
        String playerJson = objectMapper.writeValueAsString(testPlayerDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.playerId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value("Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value("Lei")
        );
    }

    @Test
    public void testThatListPlayersReturnsHttpStatus200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListPlayersReturnsListOfPlayers() throws Exception{
        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
        playerService.createPlayer(testPlayerDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/players")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].playerId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstName").value("Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].lastName").value("Lei")
        );
    }

    @Test
    public void testThatListPlayerMatchStatReturnsListOfPlayerStatDto() throws Exception{
        PlayerDto playerA = playerService.createPlayer(createTestPlayerDtoA());
        MatchPlayerDto matchPlayerDtoA = createMatchPlayerDto(playerA);
        TeamDto teamA = teamService.createTeam(createTestTeamDtoA(matchPlayerDtoA));

        PlayerDto playerB = playerService.createPlayer(createTestPlayerDtoA());
        MatchPlayerDto matchPlayerDtoB = createMatchPlayerDto(playerB);
        TeamDto teamB = teamService.createTeam(createTestTeamDtoA(matchPlayerDtoB));

        MatchDto matchA = matchService.createMatch(createTestMatchDtoA(teamA, teamB));
        MatchDto matchB = matchService.createMatch(createTestMatchDtoB(teamA, teamB));

        PlayerMatchStatDto statA = createTestPlayerMatchStatDtoA( matchA.getMatchId(),matchPlayerDtoA, teamA.getTeamId());
        statService.createPlayerMatchStat(statA);

        PlayerMatchStatDto statB = createTestPlayerMatchStatDtoB(matchA.getMatchId(),matchPlayerDtoB, teamB.getTeamId());
        statService.createPlayerMatchStat(statB);

        PlayerMatchStatDto statC = createTestPlayerMatchStatDtoA( matchB.getMatchId(), matchPlayerDtoA, teamA.getTeamId());
        statService.createPlayerMatchStat(statC);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-stats")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].playerId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstName").value("Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].lastName").value("Lei")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].potm").value(2));
    }






}
