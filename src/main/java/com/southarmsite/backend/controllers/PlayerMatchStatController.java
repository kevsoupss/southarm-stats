package com.southarmsite.backend.controllers;


import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.services.PlayerMatchStatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerMatchStatController {

    private PlayerMatchStatService playerMatchStatService;

    public PlayerMatchStatController(PlayerMatchStatService playerMatchStatService) {
        this.playerMatchStatService = playerMatchStatService;
    }

    @PostMapping(path = "/player-match-stats")
    public ResponseEntity<PlayerMatchStatDto> createPlayerMatchStat(@RequestBody PlayerMatchStatDto statDto) {
        return new ResponseEntity<>(playerMatchStatService.createPlayerMatchStat(statDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/player-match-stats")
    public ResponseEntity<List<PlayerMatchStatDto>> listAllPlayerMatchStats() {
        return ResponseEntity.ok(playerMatchStatService.findAll());
    }

    @GetMapping(path = "/player-match-stats/potm")
    public ResponseEntity<List<POTMDto>> listTopPOTM() {
        List<POTMDto> topPOTM = playerMatchStatService.findTopPOTM();
        return ResponseEntity.ok(topPOTM);
    }

    @GetMapping(path = "/player-match-stats/dotm")
    public ResponseEntity<List<DOTMDto>> listTopDOTM() {
        List<DOTMDto> topDOTM = playerMatchStatService.findTopDOTM();
        return ResponseEntity.ok(topDOTM);
    }

    @GetMapping(path= "/player-match-stats/winrates")
    public ResponseEntity<List<WinrateDto>> listTopWinrate() {
        List<WinrateDto> topWinrate = playerMatchStatService.findTopWinrate();
        return ResponseEntity.ok(topWinrate);
    }

    @GetMapping(path="/player-match-stats/scorers")
    public ResponseEntity<List<ScorerDto>> listTopScorer() {
        List<ScorerDto> topScorer = playerMatchStatService.findTopScorer();
        return ResponseEntity.ok(topScorer);
    }


}
