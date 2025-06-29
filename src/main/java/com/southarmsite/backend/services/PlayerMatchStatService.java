package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.DOTMDto;
import com.southarmsite.backend.domain.dto.POTMDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;


import java.util.List;

public interface PlayerMatchStatService {
    PlayerMatchStatDto createPlayerMatchStat(PlayerMatchStatDto playerMatchStatDto);

    List<PlayerMatchStatDto> findAll();

    List<POTMDto> findTopPOTM();

    List<DOTMDto> findTopDOTM();
}
