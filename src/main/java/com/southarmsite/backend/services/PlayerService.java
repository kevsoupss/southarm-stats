package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;

public interface PlayerService {
    PlayerDto createPlayer(PlayerDto playerDto);

}
