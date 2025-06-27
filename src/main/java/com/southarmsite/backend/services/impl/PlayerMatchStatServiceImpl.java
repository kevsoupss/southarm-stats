package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.PlayerMatchStatRepository;
import com.southarmsite.backend.services.PlayerMatchStatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlayerMatchStatServiceImpl implements PlayerMatchStatService {

    private PlayerMatchStatRepository playerMatchStatRepository;
    private Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper;

    public PlayerMatchStatServiceImpl(
            PlayerMatchStatRepository playerMatchStatRepository,
            Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper
    ) {
        this.playerMatchStatRepository = playerMatchStatRepository;
        this.playerMatchStatMapper = playerMatchStatMapper;
    }

    @Override
    public PlayerMatchStatDto createPlayerMatchStat(PlayerMatchStatDto dto) {
        PlayerMatchStatEntity entity = playerMatchStatMapper.mapFrom(dto);
        PlayerMatchStatEntity saved = playerMatchStatRepository.save(entity);
        return playerMatchStatMapper.mapTo(saved);
    }

    @Override
    public List<PlayerMatchStatDto> findAll() {
        List<PlayerMatchStatEntity> statList = StreamSupport
                .stream(playerMatchStatRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return statList.stream().map(playerMatchStatMapper::mapTo).collect(Collectors.toList());
    }

}
