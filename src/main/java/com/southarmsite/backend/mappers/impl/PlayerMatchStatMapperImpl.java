package com.southarmsite.backend.mappers.impl;

import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import com.southarmsite.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PlayerMatchStatMapperImpl implements Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> {

    private ModelMapper modelMapper;

    public PlayerMatchStatMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PlayerMatchStatDto mapTo(PlayerMatchStatEntity entity) {
        return modelMapper.map(entity, PlayerMatchStatDto.class);
    }

    @Override
    public PlayerMatchStatEntity mapFrom(PlayerMatchStatDto dto) {
        return modelMapper.map(dto, PlayerMatchStatEntity.class);
    }
}
