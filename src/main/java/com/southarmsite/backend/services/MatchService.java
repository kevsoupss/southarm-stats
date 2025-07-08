package com.southarmsite.backend.services;

import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.MatchPayloadDto;
import com.southarmsite.backend.domain.dto.MatchResponseDto;
import com.southarmsite.backend.domain.dto.MatchResultsDto;

import java.time.LocalDate;
import java.util.List;

public interface MatchService {

    MatchDto createMatch(MatchDto matchDto);

    List<MatchDto> findAll();

    List<MatchResultsDto> findAllMatchData();

    MatchResponseDto importMatch(MatchPayloadDto payload);

    void deleteByDate(LocalDate date);
}
