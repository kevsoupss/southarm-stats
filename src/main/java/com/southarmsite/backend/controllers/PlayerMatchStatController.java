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

    @PutMapping("/{id}")
    public ResponseEntity<PlayerMatchStatDto> updatePlayerMatchStat(
            @PathVariable Integer id,
            @RequestBody PlayerMatchStatDto statDto) {
        PlayerMatchStatDto updatedStat = playerMatchStatService.updatePlayerMatchStat(id, statDto);
        return ResponseEntity.ok(updatedStat);
    }

    @GetMapping(path = "/potm")
    public ResponseEntity<List<POTMDto>> listTopPOTM(@RequestParam("season") int season) {
        List<POTMDto> topPOTM = playerMatchStatService.findTopPOTM(season);
        return ResponseEntity.ok(topPOTM);
    }

    @GetMapping(path = "/dotm")
    public ResponseEntity<List<DOTMDto>> listTopDOTM(@RequestParam("season") int season) {
        List<DOTMDto> topDOTM = playerMatchStatService.findTopDOTM(season);
        return ResponseEntity.ok(topDOTM);
    }

    @GetMapping(path= "/winrates")
    public ResponseEntity<List<WinrateDto>> listTopWinrate(@RequestParam("season") int season) {
        List<WinrateDto> topWinrate = playerMatchStatService.findTopWinrate(season);
        return ResponseEntity.ok(topWinrate);
    }

    @GetMapping(path="/scorers")
    public ResponseEntity<List<ScorerDto>> listTopScorer(@RequestParam("season") int season) {
        List<ScorerDto> topScorer = playerMatchStatService.findTopScorer(season);
        return ResponseEntity.ok(topScorer);
    }

    @GetMapping(path="/assisters")
    public ResponseEntity<List<AssisterDto>> listTopAssisters(@RequestParam("season") int season) {
        List<AssisterDto> topScorer = playerMatchStatService.findTopAssister(season);
        return ResponseEntity.ok(topScorer);
    }

    @GetMapping(path="/winstreaks")
    public ResponseEntity<List<WinStreakDto>> listTopWinStreakers(@RequestParam("season") int season) {
        List<WinStreakDto> topWinStreakers = playerMatchStatService.findTopWinStreakers(season);
        return ResponseEntity.ok(topWinStreakers);
    }




}
