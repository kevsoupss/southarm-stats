package com.southarmsite.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.services.MatchService;
import com.southarmsite.backend.services.PlayerMatchStatService;
import com.southarmsite.backend.services.PlayerService;
import com.southarmsite.backend.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.southarmsite.backend.TestDataUtil.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PlayerMatchStatControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final PlayerMatchStatService playerMatchStatService;
    private final PlayerService playerService;
    private final TeamService teamService;
    private final MatchService matchService;
    private final ObjectMapper objectMapper;

    private PlayerDto testPlayerDtoA;
    private PlayerDto testPlayerDtoB;
    private TeamDto testTeamDtoA;
    private TeamDto testTeamDtoB;
    private TeamDto testTeamDtoC;
    private TeamDto testTeamDtoD;
    private MatchDto testMatchDtoA;
    private MatchDto testMatchDtoB;

    @Autowired
    public PlayerMatchStatControllerIntegrationTest(
            MockMvc mockMvc,
            PlayerMatchStatService playerMatchStatService,
            PlayerService playerService,
            MatchService matchService,
            TeamService teamService
    ) {
        this.mockMvc = mockMvc;
        this.playerMatchStatService = playerMatchStatService;
        this.playerService = playerService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setUp() {
        testPlayerDtoA = playerService.createPlayer(createTestPlayerDtoA());
        testTeamDtoA = teamService.createTeam(createTestTeamDtoA(createMatchPlayerDto(testPlayerDtoA)));

        testPlayerDtoB = playerService.createPlayer(createTestPlayerDtoB());
        testTeamDtoB = teamService.createTeam(createTestTeamDtoB(createMatchPlayerDto(testPlayerDtoB)));
        testTeamDtoC = teamService.createTeam(createTestTeamDtoB(createMatchPlayerDto(testPlayerDtoB)));
        testTeamDtoD = teamService.createTeam(createTestTeamDtoB(createMatchPlayerDto(testPlayerDtoB)));

        testMatchDtoA = matchService.createMatch(createTestMatchDtoA(testTeamDtoA, testTeamDtoB));
        testMatchDtoB = matchService.createMatch(createTestMatchDtoA(testTeamDtoC, testTeamDtoD));
    }

    @Test
    public void testThatCreatePlayerMatchStatReturns201() throws Exception {
        PlayerMatchStatDto statDto = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(),createMatchPlayerDto(testPlayerDtoA), testTeamDtoA.getTeamId());
        statDto.setPlayerMatchStatId(null);
        String json = objectMapper.writeValueAsString(statDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/player-match-stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatePlayerMatchStatReturnsSavedStat() throws Exception {
        PlayerMatchStatDto statDto = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoA.getTeamId());
        statDto.setPlayerMatchStatId(null);
        String json = objectMapper.writeValueAsString(statDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/player-match-stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.matchId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.goals").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.player.firstName").value("Kevin")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatListPlayerMatchStatsReturns200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListPlayerMatchStatsReturnsList() throws Exception {
        PlayerMatchStatDto statDto = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoA.getTeamId());
        statDto.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].matchId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].goals").value(statDto.getGoals())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].player.firstName").value("Kevin")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatFindTopPOTMReturnsList() throws Exception {
        PlayerMatchStatDto statDtoA = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoA.getTeamId());
        PlayerMatchStatDto statDtoB = createTestPlayerMatchStatDtoB(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoB.getTeamId());
        PlayerMatchStatDto statDtoC = createTestPlayerMatchStatDtoB(testMatchDtoB.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoB.getTeamId());
        statDtoA.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoA);
        statDtoB.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoB);
        statDtoC.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoC);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats/potm")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Kevin Lei")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].potm").value(2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].name").value("Ronald Lam")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatFindTopDOTMReturnsList() throws Exception {
        PlayerMatchStatDto statDtoA = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoA.getTeamId());
        PlayerMatchStatDto statDtoB = createTestPlayerMatchStatDtoB(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoB.getTeamId());
        PlayerMatchStatDto statDtoC = createTestPlayerMatchStatDtoA(testMatchDtoB.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoB.getTeamId());
        statDtoA.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoA);
        statDtoB.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoB);
        statDtoC.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoC);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats/dotm")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Ronald Lam")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].dotm").value(2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].name").value("Kevin Lei")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatFindTopWinrateReturnsList() throws Exception {
        PlayerMatchStatDto statDtoA = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoA.getTeamId());
        PlayerMatchStatDto statDtoB = createTestPlayerMatchStatDtoB(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoB.getTeamId());
        PlayerMatchStatDto statDtoC = createTestPlayerMatchStatDtoB(testMatchDtoB.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoB.getTeamId());
        statDtoA.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoA);
        statDtoB.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoB);
        statDtoC.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoC);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats/winrates")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Kevin Lei")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].winrate").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].wins").value("1")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatFindTopScorerReturnsList() throws Exception {
        PlayerMatchStatDto statDtoA = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoA.getTeamId());
        PlayerMatchStatDto statDtoB = createTestPlayerMatchStatDtoB(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoB.getTeamId());
        PlayerMatchStatDto statDtoC = createTestPlayerMatchStatDtoB(testMatchDtoB.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoB.getTeamId());
        statDtoA.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoA);
        statDtoB.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoB);
        statDtoC.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoC);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats/scorers")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Ronald Lam")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].goals").value(3)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].wins").value("1")
        ).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testThatFindTopWinStreakersReturnsList() throws Exception {
        PlayerMatchStatDto statDtoA = createTestPlayerMatchStatDtoA(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoA.getTeamId());
        PlayerMatchStatDto statDtoB = createTestPlayerMatchStatDtoB(testMatchDtoA.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoB.getTeamId());
        PlayerMatchStatDto statDtoC = createTestPlayerMatchStatDtoB(testMatchDtoB.getMatchId(), createMatchPlayerDto(testPlayerDtoB), testTeamDtoB.getTeamId());
        PlayerMatchStatDto statDtoD = createTestPlayerMatchStatDtoB(testMatchDtoB.getMatchId(), createMatchPlayerDto(testPlayerDtoA), testTeamDtoB.getTeamId());
        statDtoA.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoA);
        statDtoB.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoB);
        statDtoC.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoC);
        statDtoD.setPlayerMatchStatId(null);
        playerMatchStatService.createPlayerMatchStat(statDtoD);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/player-match-stats/winstreaks")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Kevin Lei")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].winStreak").value(2)
        ).andDo(MockMvcResultHandlers.print());
    }



}
