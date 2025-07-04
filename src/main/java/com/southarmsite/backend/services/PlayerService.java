package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerStatsDto;
import com.southarmsite.backend.domain.dto.WinStreakDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;

import java.util.List;

public interface PlayerService {
    PlayerDto createPlayer(PlayerDto playerDto);

    List<PlayerDto> findAll();

    List<PlayerStatsDto> findAllPlayerStats();

}
