package com.southarmsite.backend.mappers.impl;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
import com.southarmsite.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TeamMapperImpl implements Mapper<TeamEntity, TeamDto> {

    private ModelMapper modelMapper;

    public TeamMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TeamDto mapTo(TeamEntity teamEntity) {
        return modelMapper.map(teamEntity, TeamDto.class);
    }

    @Override
    public TeamEntity mapFrom(TeamDto teamDto) {
        return modelMapper.map(teamDto, TeamEntity.class);
    }

}
