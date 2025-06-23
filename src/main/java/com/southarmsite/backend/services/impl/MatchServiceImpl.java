package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.MatchRepository;
import com.southarmsite.backend.services.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private Mapper<MatchEntity, MatchDto> matchMapper;

    public MatchServiceImpl(MatchRepository matchRepository, Mapper<MatchEntity, MatchDto> matchMapper){
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
    }

    @Override
    public MatchDto createMatch(MatchDto matchDto) {
        MatchEntity matchEntity = matchMapper.mapFrom(matchDto);
        MatchEntity savedMatchEntity = matchRepository.save(matchEntity);
        return matchMapper.mapTo(savedMatchEntity);
    }

    @Override
    public List<MatchDto> findAll() {
        List<MatchEntity> matchEntityList = StreamSupport.stream(matchRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return matchEntityList.stream().map(matchMapper::mapTo).collect(Collectors.toList());
    }
}
