package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.GoalDto;
import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.entities.GoalEntity;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.GoalRepository;
import com.southarmsite.backend.services.GoalService;

public class GoalServiceImpl implements GoalService {

    private GoalRepository goalRepository;
    private Mapper<GoalEntity, GoalDto> goalMapper;

    public GoalServiceImpl(GoalRepository goalRepository, Mapper<GoalEntity, GoalDto> goalMapper){
        this.goalRepository = goalRepository;
        this.goalMapper = goalMapper;
    }
    @Override
    public GoalDto createGoal(GoalDto goalDto) {
        GoalEntity goalEntity = goalMapper.mapFrom(goalDto);
        GoalEntity savedGoalEntity = goalRepository.save(goalEntity);
        return goalMapper.mapTo(savedGoalEntity);
    }


}
