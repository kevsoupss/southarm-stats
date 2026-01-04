package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.*;


import java.util.List;

public interface PlayerMatchStatService {
    PlayerMatchStatDto createPlayerMatchStat(PlayerMatchStatDto playerMatchStatDto);

    List<PlayerMatchStatDto> findAll();

    List<POTMDto> findTopPOTM(int season);

    List<DOTMDto> findTopDOTM(int season);

    List<WinrateDto> findTopWinrate(int season);

    List<ScorerDto> findTopScorer(int season);

    List<AssisterDto> findTopAssister(int season);

    List<WinStreakDto> findTopWinStreakers(int season);

    PlayerMatchStatDto updatePlayerMatchStat(Integer id, PlayerMatchStatDto statDto);


}
