package com.southarmsite.backend.mappers.impl;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapperImpl implements Mapper<PlayerEntity, PlayerDto> {

    private ModelMapper modelMapper;

    public PlayerMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PlayerDto mapTo(PlayerEntity playerEntity) {
        return modelMapper.map(playerEntity, PlayerDto.class);
    }

    @Override
    public PlayerEntity mapFrom(PlayerDto playerDto) {
        return modelMapper.map(playerDto, PlayerEntity.class);
    }
}
