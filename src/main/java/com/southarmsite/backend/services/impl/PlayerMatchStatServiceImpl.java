package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.MatchRepository;
import com.southarmsite.backend.repositories.PlayerMatchStatRepository;
import com.southarmsite.backend.repositories.TeamRepository;
import com.southarmsite.backend.services.PlayerMatchStatService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlayerMatchStatServiceImpl implements PlayerMatchStatService {

    private PlayerMatchStatRepository playerMatchStatRepository;
    private MatchRepository matchRepository;
    private TeamRepository teamRepository;
    private Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper;

    public PlayerMatchStatServiceImpl(
            PlayerMatchStatRepository playerMatchStatRepository,
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper
    ) {
        this.playerMatchStatRepository = playerMatchStatRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerMatchStatMapper = playerMatchStatMapper;

    }

    @Override
    public PlayerMatchStatDto createPlayerMatchStat(PlayerMatchStatDto dto) {
        PlayerMatchStatEntity entity = playerMatchStatMapper.mapFrom(dto);
        if (dto.getMatchId() != null) {
            MatchEntity matchEntity = matchRepository.findById(dto.getMatchId())
                    .orElseThrow(() -> new EntityNotFoundException("Match not found"));
           entity.setMatch(matchEntity);
        }
        if (dto.getTeamId() != null) {
            TeamEntity teamEntity = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            entity.setTeam(teamEntity);
        }

        PlayerMatchStatEntity saved = playerMatchStatRepository.save(entity);
        return playerMatchStatMapper.mapTo(saved);
    }

    @Override
    public List<PlayerMatchStatDto> findAll() {
        List<PlayerMatchStatEntity> statList = StreamSupport
                .stream(playerMatchStatRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return statList.stream().map(playerMatchStatMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public List<POTMDto> findTopPOTM() {
        List<POTMDto> topPOTM = StreamSupport
                .stream(playerMatchStatRepository.findTopPOTM().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topPOTM;
    }

    @Override
    public List<DOTMDto> findTopDOTM() {
        List<DOTMDto> topDOTM = StreamSupport
                .stream(playerMatchStatRepository.findTopDOTM().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topDOTM;
    }

    @Override
    public List<WinrateDto> findTopWinrate() {
        List<WinrateDto> topWinrate = StreamSupport
                .stream(playerMatchStatRepository.findTopWinrate().spliterator(), false)
                .collect(Collectors.toList());
        return topWinrate;
    }

    @Override
    public List<ScorerDto> findTopScorer() {
        List<ScorerDto> topScorer = StreamSupport
                .stream(playerMatchStatRepository.findTopScorer().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topScorer;
    }

    public List<WinStreakDto> findTopWinStreakers() {
        List<WinStreakDto> topWinStreakers = StreamSupport
                .stream(playerMatchStatRepository.getTop5WinStreakers().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topWinStreakers;
    }
}
