package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerStatsDto;
import com.southarmsite.backend.domain.dto.WinStreakDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;

import java.util.List;
import java.util.Optional;

public interface PlayerService {
    PlayerDto createPlayer(PlayerDto playerDto);

    List<PlayerDto> findAll();

    List<PlayerStatsDto> findAllPlayerStats();

    List<PlayerDto> savePlayers(List<PlayerDto> playersPayload);

    PlayerDto findByFirstNameAndLastName(String firstName, String lastName);

    PlayerDto findByFirstName(String firstName);

    void mergePlayers(Integer duplicatePlayerId, Integer canonicalPlayerId);
}
