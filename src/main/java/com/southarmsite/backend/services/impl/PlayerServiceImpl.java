package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.PlayerRepository;
import com.southarmsite.backend.services.PlayerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;
    private Mapper<PlayerEntity, PlayerDto> playerMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, Mapper<PlayerEntity, PlayerDto> playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        PlayerEntity playerEntity = playerMapper.mapFrom(playerDto);
        PlayerEntity savedPlayerEntity = playerRepository.save(playerEntity);
        return playerMapper.mapTo(savedPlayerEntity);
    }

    @Override
    public List<PlayerDto> findAll() {
        List<PlayerEntity> playerEntityList = StreamSupport.stream(playerRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return playerEntityList.stream().map(playerMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<PlayerDto> findAllByName(String name) {
        List<PlayerEntity> playerEntityList = StreamSupport.stream(playerRepository.findAllByFirstNameContainingIgnoreCase(name).spliterator(), false).collect(Collectors.toList());
        return playerEntityList.stream().map(playerMapper::mapTo).collect(Collectors.toList());
    }
}
