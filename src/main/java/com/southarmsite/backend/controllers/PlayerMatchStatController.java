package com.southarmsite.backend.controllers;


import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.services.PlayerMatchStatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player-match-stats")
public class PlayerMatchStatController {

    private PlayerMatchStatService playerMatchStatService;

    public PlayerMatchStatController(PlayerMatchStatService playerMatchStatService) {
        this.playerMatchStatService = playerMatchStatService;
    }

    @PostMapping
    public ResponseEntity<PlayerMatchStatDto> createPlayerMatchStat(@RequestBody PlayerMatchStatDto statDto) {
        return new ResponseEntity<>(playerMatchStatService.createPlayerMatchStat(statDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlayerMatchStatDto>> listAllPlayerMatchStats() {
        return ResponseEntity.ok(playerMatchStatService.findAll());
    }

    @GetMapping(path = "/potm")
    public ResponseEntity<List<POTMDto>> listTopPOTM() {
        List<POTMDto> topPOTM = playerMatchStatService.findTopPOTM();
        return ResponseEntity.ok(topPOTM);
    }

    @GetMapping(path = "/dotm")
    public ResponseEntity<List<DOTMDto>> listTopDOTM() {
        List<DOTMDto> topDOTM = playerMatchStatService.findTopDOTM();
        return ResponseEntity.ok(topDOTM);
    }

    @GetMapping(path= "/winrates")
    public ResponseEntity<List<WinrateDto>> listTopWinrate() {
        List<WinrateDto> topWinrate = playerMatchStatService.findTopWinrate();
        return ResponseEntity.ok(topWinrate);
    }

    @GetMapping(path="/scorers")
    public ResponseEntity<List<ScorerDto>> listTopScorer() {
        List<ScorerDto> topScorer = playerMatchStatService.findTopScorer();
        return ResponseEntity.ok(topScorer);
    }

    @GetMapping(path="/winstreaks")
    public ResponseEntity<List<WinStreakDto>> listTopWinStreakers() {
        List<WinStreakDto> topWinStreakers = playerMatchStatService.findTopWinStreakers();
        return ResponseEntity.ok(topWinStreakers);
    }


}
