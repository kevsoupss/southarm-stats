package com.southarmsite.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.repositories.MatchRepository;
import com.southarmsite.backend.services.MatchService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static com.southarmsite.backend.TestDataUtil.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MatchControllerIntegrationTest {


    private MatchService matchService;
    private TeamService teamService;
    private PlayerService playerService;
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public MatchControllerIntegrationTest(MockMvc mockMvc, MatchService matchService, TeamService teamService, PlayerService playerService) {
        this.mockMvc = mockMvc;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    public void testThatCreateMatchSuccessfullyReturnsHttp201Created() throws Exception{
        PlayerDto testPlayerDtoA = playerService.createPlayer(createTestPlayerDtoA());
        TeamDto testTeamDtoA = teamService.createTeam(createTestTeamDtoA(testPlayerDtoA));
        PlayerDto testPlayerDtoB = playerService.createPlayer(createTestPlayerDtoB());
        TeamDto testTeamDtoB = teamService.createTeam(createTestTeamDtoB(testPlayerDtoB));

        MatchDto testMatchDtoA = createTestMatchDtoA(testTeamDtoA,testTeamDtoB);
        testMatchDtoA.setMatchId(null);
        String matchJson = objectMapper.writeValueAsString(testMatchDtoA);
        System.out.println(matchJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(matchJson)

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateMatchSuccessfullyReturnsSavedMatch() throws Exception{
        PlayerDto testPlayerDtoA = playerService.createPlayer(createTestPlayerDtoA());
        TeamDto testTeamDtoA = teamService.createTeam(createTestTeamDtoA(testPlayerDtoA));
        PlayerDto testPlayerDtoB = playerService.createPlayer(createTestPlayerDtoB());
        TeamDto testTeamDtoB = teamService.createTeam(createTestTeamDtoB(testPlayerDtoB));

        MatchDto testMatchDtoA = createTestMatchDtoA(testTeamDtoA,testTeamDtoB);
        testMatchDtoA.setMatchId(null);
        String matchJson = objectMapper.writeValueAsString(testMatchDtoA);
        System.out.println(matchJson);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(matchJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.matchId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.date").value("2025-05-20")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value("Southarm")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.scoreA").value(5)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.scoreB").value(3)
        );
    }

    @Test
    public void testThatListsMatchesReturnsHttp200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/matches")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatListsMatchesReturnsList() throws Exception{
        PlayerDto testPlayerDtoA = playerService.createPlayer(createTestPlayerDtoA());
        TeamDto testTeamDtoA = teamService.createTeam(createTestTeamDtoA(testPlayerDtoA));
        PlayerDto testPlayerDtoB = playerService.createPlayer(createTestPlayerDtoB());
        TeamDto testTeamDtoB = teamService.createTeam(createTestTeamDtoB(testPlayerDtoB));

        MatchDto testMatchDtoA = createTestMatchDtoA(testTeamDtoA,testTeamDtoB);
        testMatchDtoA.setMatchId(null);

        matchService.createMatch(testMatchDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/matches")
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].matchId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].date").value("2025-05-20")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].location").value("Southarm")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].scoreA").value(5)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].scoreB").value(3)
        );

    }


}
