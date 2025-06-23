package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.MatchDto;

import java.util.List;

public interface MatchService {

    MatchDto createMatch(MatchDto matchDto);

    List<MatchDto> findAll();
}
