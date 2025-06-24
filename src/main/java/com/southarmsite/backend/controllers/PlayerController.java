package com.southarmsite.backend.controllers;


import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;

    }

    @PostMapping(path="/players")
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        return new ResponseEntity<>(playerService.createPlayer(playerDto), HttpStatus.CREATED);
    }

    @GetMapping(path="/players")
    public ResponseEntity<List<PlayerDto>> listPlayers() {
        List<PlayerDto> players = playerService.findAll();
        return ResponseEntity.ok(players);
    }

    @GetMapping("/players/by-name")
    public ResponseEntity<List<PlayerDto>> getPlayersByName(@RequestParam String name) {
        return ResponseEntity.ok(playerService.findAllByName(name));
    }
}
