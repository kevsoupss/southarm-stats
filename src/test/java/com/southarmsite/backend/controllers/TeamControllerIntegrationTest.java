package com.southarmsite.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerSummaryDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.services.MatchService;
import com.southarmsite.backend.services.PlayerService;
import com.southarmsite.backend.services.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.southarmsite.backend.TestDataUtil.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TeamControllerIntegrationTest {

    private TeamService teamService;
    private PlayerService playerService;
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public TeamControllerIntegrationTest(MockMvc mockMvc, TeamService teamService, PlayerService playerService) {
        this.mockMvc = mockMvc;
        this.teamService = teamService;
        this.playerService = playerService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateTeamSuccessfullyReturnsHttp201Created() throws Exception{
        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
        PlayerDto savedPlayerDtoA = playerService.createPlayer(testPlayerDtoA);

        TeamDto testTeamDtoA = createTestTeamDtoA(savedPlayerDtoA);
        testTeamDtoA.setTeamId(null);
        String matchJson = objectMapper.writeValueAsString(testTeamDtoA);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(matchJson)

        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateTeamSuccessfullyReturnsSavedTeam() throws Exception{
        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
        PlayerDto savedPlayerDtoA = playerService.createPlayer(testPlayerDtoA);

        TeamDto testTeamDtoA = createTestTeamDtoA(savedPlayerDtoA);
        testTeamDtoA.setTeamId(null);
        String matchJson = objectMapper.writeValueAsString(testTeamDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(matchJson)

        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.teamId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Team Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.teamId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.captain.firstName").value("Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.captain.position").value("Winger")
        );



    }

    @Test
    public void testThatListTeamsReturnsHttpStatus200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListTeamsReturnsListOfTeams() throws Exception{
        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
        PlayerDto savedPlayerDtoA = playerService.createPlayer(testPlayerDtoA);

        TeamDto testTeamDtoA = createTestTeamDtoA(savedPlayerDtoA);
        teamService.createTeam(testTeamDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].teamId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Team Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].teamId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].captain.firstName").value("Kevin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].captain.position").value("Winger")
        );
    }
}
