package com.southarmsite.backend.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.services.PlayerService;
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

import static com.southarmsite.backend.TestDataUtil.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTest {

    private PlayerService playerService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public PlayerControllerIntegrationTest(MockMvc mockMvc, PlayerService playerService) {
        this.mockMvc = mockMvc;
        this.playerService = playerService;
        this.objectMapper = new ObjectMapper();
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

//    @Test
//    public void testThatListPlayerMatchStatReturnsListOfPlayerStatDto() throws Exception{
//        PlayerDto testPlayerDtoA = createTestPlayerDtoA();
//        playerService.createPlayer(testPlayerDtoA);
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].playerId").isNumber()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].firstName").value("Kevin")
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0].lastName").value("Lei")
//        );
//    }






}
