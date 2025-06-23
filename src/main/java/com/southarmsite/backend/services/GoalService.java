package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.GoalDto;

public interface GoalService {
    GoalDto createGoal(GoalDto goalDto);
}
