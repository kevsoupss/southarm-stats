package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.MatchDto;

public interface MatchService {

    MatchDto createMatch(MatchDto matchDto);
}
