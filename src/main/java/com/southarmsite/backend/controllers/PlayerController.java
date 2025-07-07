package com.southarmsite.backend.controllers;


import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerStatsDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;

    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.createPlayer(playerDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> listPlayers() {
        List<PlayerDto> players = playerService.findAll();
        return ResponseEntity.ok(players);
    }

    @GetMapping(path="/stats")
    public ResponseEntity<List<PlayerStatsDto>> listPlayerStats() {
        List<PlayerStatsDto> playerStats = playerService.findAllPlayerStats();
        return ResponseEntity.ok(playerStats);
    }

    @PostMapping(path = "/payload")
    public ResponseEntity<List<PlayerDto>> savePlayers(@RequestBody List<PlayerDto> playersPayload) {
        List<PlayerDto> savedPlayerResponse = playerService.savePlayers(playersPayload);
        return ResponseEntity.ok(savedPlayerResponse);
    }

}
