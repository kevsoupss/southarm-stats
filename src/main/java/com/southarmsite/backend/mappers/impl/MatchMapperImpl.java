package com.southarmsite.backend.mappers.impl;

import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MatchMapperImpl implements Mapper<MatchEntity, MatchDto> {

    ModelMapper modelMapper;

    public MatchMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MatchDto mapTo(MatchEntity matchEntity) {
        return modelMapper.map(matchEntity, MatchDto.class);
    }

    @Override
    public MatchEntity mapFrom(MatchDto matchDto) {
        return modelMapper.map(matchDto, MatchEntity.class);
    }
}
