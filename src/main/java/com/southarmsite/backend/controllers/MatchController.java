package com.southarmsite.backend.controllers;

import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.services.MatchService;
import com.southarmsite.backend.services.PlayerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatchController {

    private MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }


    @PostMapping(path="/matches")
    public ResponseEntity<MatchDto> createMatch(@RequestBody MatchDto matchDto){
        return new ResponseEntity<>(matchService.createMatch(matchDto), HttpStatus.CREATED);
    }

    @GetMapping(path="/matches")
    public List<MatchDto> listMatches(){
        List<MatchDto> matches = matchService.findAll();
        return matches;
    }


    @GetMapping(path="/matches/data")
    public ResponseEntity<List<MatchResultsDto>> listMatchData() {
        List<MatchResultsDto> listMatchData = matchService.findAllMatchData();
        return ResponseEntity.ok(listMatchData);
    }

    @PostMapping(path="/matches/results")
    public ResponseEntity<MatchResponseDto> importMatch(@RequestBody MatchPayloadDto payload) {
        return new ResponseEntity<>(matchService.importMatch(payload), HttpStatus.CREATED);
    }
}
