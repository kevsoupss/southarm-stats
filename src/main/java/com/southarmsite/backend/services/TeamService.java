package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.TeamDto;

import java.util.List;

public interface TeamService {
    TeamDto createTeam(TeamDto teamDto);
    List<TeamDto> findAll();

}
