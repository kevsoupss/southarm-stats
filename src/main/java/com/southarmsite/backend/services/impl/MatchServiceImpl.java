package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.MatchDto;
import com.southarmsite.backend.domain.dto.MatchResultsDto;
import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.MatchRepository;
import com.southarmsite.backend.repositories.PlayerMatchStatRepository;
import com.southarmsite.backend.services.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private PlayerMatchStatRepository playerMatchStatRepository;
    private Mapper<MatchEntity, MatchDto> matchMapper;
    private Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper;

    public MatchServiceImpl(MatchRepository matchRepository,
                            PlayerMatchStatRepository playerMatchStatRepository,
                            Mapper<MatchEntity, MatchDto> matchMapper,
                            Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper){
        this.matchRepository = matchRepository;
        this.playerMatchStatRepository = playerMatchStatRepository;
        this.matchMapper = matchMapper;
        this.playerMatchStatMapper = playerMatchStatMapper;
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

    @Override
    public List<MatchResultsDto> getRecentMatches() {
        List<MatchEntity> recentMatches = matchRepository.findTop5ByOrderByDateDesc();

        return recentMatches.stream()
                .map(match -> {
                    List<PlayerMatchStatEntity> stats = playerMatchStatRepository.findByMatch(match);

                    List<PlayerMatchStatDto> statDto = stats.stream().map(playerMatchStatMapper::mapTo).toList();

                    return MatchResultsDto.builder()
                            .matchId(match.getMatchId())
                            .teamA(match.getTeamA().getName())
                            .teamB(match.getTeamB().getName())
                            .scoreA(match.getScoreA())
                            .scoreB(match.getScoreB())
                            .date(match.getDate().toString())
                            .location(match.getLocation())
                            .playerStats(statDto)
                            .build();
                }


                ).toList();
    }
}
