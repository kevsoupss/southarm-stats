package com.southarmsite.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.repositories.MatchRepository;
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
import java.time.LocalDate;
import static com.southarmsite.backend.TestDataUtil.*;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MatchControllerIntegrationTest {

    private MatchService matchService;
    private TeamService teamService;
    private PlayerService playerService;
    private PlayerMatchStatService statService;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @Autowired
    public MatchControllerIntegrationTest(
            MockMvc mockMvc,
            MatchService matchService,
            TeamService teamService,
            PlayerService playerService,
            PlayerMatchStatService statService) {
        this.mockMvc = mockMvc;
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.statService = statService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    private PlayerDto testPlayerDtoA;
    private PlayerDto testPlayerDtoB;
    private TeamDto testTeamDtoA;
    private TeamDto testTeamDtoB;
    private MatchDto testMatchDtoA;

    @BeforeEach
    public void setupTestData() {
        testPlayerDtoA = playerService.createPlayer(createTestPlayerDtoA());
        testPlayerDtoB = playerService.createPlayer(createTestPlayerDtoB());

        testTeamDtoA = teamService.createTeam(createTestTeamDtoA(createMatchPlayerDto(testPlayerDtoA)));
        testTeamDtoB = teamService.createTeam(createTestTeamDtoB(createMatchPlayerDto(testPlayerDtoB)));

        testMatchDtoA = createTestMatchDtoA(testTeamDtoA, testTeamDtoB);
        testMatchDtoA.setMatchId(null);
    }

    @Test
    public void testThatCreateMatchSuccessfullyReturnsHttp201Created() throws Exception {
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
    public void testThatCreateMatchSuccessfullyReturnsSavedMatch() throws Exception {
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
    public void testThatListsMatchesReturnsList() throws Exception {
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


    @Test
    public void testThatGetAllMatchesReturnsCorrectData() throws Exception {
        MatchDto savedMatch = matchService.createMatch(testMatchDtoA);

        PlayerMatchStatDto statA = createTestPlayerMatchStatDtoA(
                savedMatch.getMatchId(),
                createMatchPlayerDto(testPlayerDtoA),
                testTeamDtoA.getTeamId()
        );
        statService.createPlayerMatchStat(statA);

        PlayerMatchStatDto statB = createTestPlayerMatchStatDtoB(
                savedMatch.getMatchId(),
                createMatchPlayerDto(testPlayerDtoB),
                testTeamDtoB.getTeamId()
        );
        statService.createPlayerMatchStat(statB);


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/matches/data")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].matchId").value(savedMatch.getMatchId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teamA").value(testTeamDtoA.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teamB").value(testTeamDtoB.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].scoreA").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].playersA[0].player.firstName").value(testPlayerDtoA.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].playersB[0].player.firstName").value(testPlayerDtoB.getFirstName()))
                .andDo(MockMvcResultHandlers.print());
    }
}
