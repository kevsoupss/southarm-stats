package com.southarmsite.backend.mappers.impl;

import com.southarmsite.backend.domain.dto.GoalDto;
import com.southarmsite.backend.domain.entities.GoalEntity;
import com.southarmsite.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GoalMapperImpl implements Mapper<GoalEntity, GoalDto> {

    ModelMapper modelMapper;

    public GoalMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GoalDto mapTo(GoalEntity goalEntity) {
        return modelMapper.map(goalEntity, GoalDto.class);
    }

    @Override
    public GoalEntity mapFrom(GoalDto goalDto) {
        return modelMapper.map(goalDto, GoalEntity.class);
    }
}