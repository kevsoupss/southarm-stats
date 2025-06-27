package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.TeamDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.TeamRepository;
import com.southarmsite.backend.services.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;
    private Mapper<TeamEntity, TeamDto> teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository, Mapper<TeamEntity, TeamDto> teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public TeamDto createTeam(TeamDto teamDto) {

        TeamEntity teamEntity = teamMapper.mapFrom(teamDto);
        TeamEntity savedTeamEntity = teamRepository.save(teamEntity);
        return teamMapper.mapTo(savedTeamEntity);
    }

    @Override
    public List<TeamDto> findAll() {
        List<TeamEntity> teamEntityList = StreamSupport.stream(teamRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return teamEntityList.stream().map(teamMapper::mapTo).collect(Collectors.toList());
    }
}
