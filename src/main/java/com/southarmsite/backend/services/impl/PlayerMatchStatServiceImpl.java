package com.southarmsite.backend.services.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value= "topPOTM", key="'all'")
    @Override
    public List<POTMDto> findTopPOTM() {
        System.out.println("--- Executing findTopPOTM from DB ---");
        List<POTMDto> topPOTM = StreamSupport
                .stream(playerMatchStatRepository.findTopPOTM().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topPOTM;
    }

    @Cacheable(value= "topDOTM", key="'all'")
    @Override
    public List<DOTMDto> findTopDOTM() {
        System.out.println("--- Executing findTopDOTM from DB ---");
        List<DOTMDto> topDOTM = StreamSupport
                .stream(playerMatchStatRepository.findTopDOTM().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topDOTM;
    }

    @Cacheable(value= "topWinrate", key="'all'")
    @Override
    public List<WinrateDto> findTopWinrate() {
        System.out.println("--- Executing findTopWR from DB ---");
        List<WinrateDto> topWinrate = StreamSupport
                .stream(playerMatchStatRepository.findTopWinrate().spliterator(), false)
                .collect(Collectors.toList());
        return topWinrate;
    }

    @Cacheable(value= "topScorers", key="'all'")
    @Override
    public List<ScorerDto> findTopScorer() {
        System.out.println("--- Executing findTopScorer from DB ---");
        List<ScorerDto> topScorer = StreamSupport
                .stream(playerMatchStatRepository.findTopScorer().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topScorer;
    }

    @Cacheable(value= "topAssisters", key="'all'")
    @Override
    public List<AssisterDto> findTopAssister() {
        System.out.println("--- Executing findTopAssister from DB ---");
        List<AssisterDto> topAssister = StreamSupport
                .stream(playerMatchStatRepository.findTopAssisters().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topAssister;
    }

    @Cacheable(value= "topWinStreakers", key="'all'")
    @Override
    public List<WinStreakDto> findTopWinStreakers() {
        System.out.println("--- Executing findTopWinStreaker from DB ---");
        List<WinStreakDto> topWinStreakers = StreamSupport
                .stream(playerMatchStatRepository.getTop5WinStreakers().spliterator(), false)
                .limit(5)
                .collect(Collectors.toList());
        return topWinStreakers;
    }

    @Override
    public PlayerMatchStatDto updatePlayerMatchStat(Integer id, PlayerMatchStatDto statDto) {
        PlayerMatchStatEntity existingStat = playerMatchStatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlayerMatchStat not found with id: " + id));

        existingStat.setGoals(statDto.getGoals());
        existingStat.setAssists(statDto.getAssists());
        existingStat.setPotm(statDto.getPotm());
        existingStat.setDotm(statDto.getDotm());

        PlayerMatchStatEntity updated = playerMatchStatRepository.save(existingStat);

        return playerMatchStatMapper.mapTo(updated);
    }

    @CacheEvict(value = {"topPOTM", "topDOTM", "topWinrate", "topScorers", "topAssisters", "topWinStreakers", "players", "recentMatches"}, allEntries = true)
    public void evictAllLeaderboardCaches() {
        System.out.println("Evicting all leaderboard caches (PlayerMatchStatService)...");
    }
}
