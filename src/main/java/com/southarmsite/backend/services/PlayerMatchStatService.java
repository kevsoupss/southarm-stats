package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.*;


import java.util.List;

public interface PlayerMatchStatService {
    PlayerMatchStatDto createPlayerMatchStat(PlayerMatchStatDto playerMatchStatDto);

    List<PlayerMatchStatDto> findAll();

    List<POTMDto> findTopPOTM();

    List<DOTMDto> findTopDOTM();

    List<WinrateDto> findTopWinrate();

    List<ScorerDto> findTopScorer();

    List<AssisterDto> findTopAssister();

    List<WinStreakDto> findTopWinStreakers();

    PlayerMatchStatDto updatePlayerMatchStat(Integer id, PlayerMatchStatDto statDto);


}
