package com.southarmsite.backend.controllers;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.services.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(path="/teams")
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        return new ResponseEntity<>(teamService.createTeam(teamDto), HttpStatus.CREATED);
    }

    @GetMapping(path="/teams")
    public ResponseEntity<List<TeamDto>> listTeams() {
        List<TeamDto> teamList = teamService.findAll();
        return ResponseEntity.ok(teamList);
    }

}
