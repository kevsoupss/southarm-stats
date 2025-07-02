package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.MatchRepository;
import com.southarmsite.backend.repositories.PlayerMatchStatRepository;
import com.southarmsite.backend.repositories.PlayerRepository;
import com.southarmsite.backend.repositories.TeamRepository;
import com.southarmsite.backend.services.MatchService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MatchServiceImpl implements MatchService {

    private MatchRepository matchRepository;
    private PlayerMatchStatRepository playerMatchStatRepository;
    private TeamRepository teamRepository;
    private PlayerRepository playerRepository;
    private Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper;
    private Mapper<MatchEntity, MatchDto> matchMapper;

    public MatchServiceImpl(MatchRepository matchRepository,
                            PlayerMatchStatRepository playerMatchStatRepository,
                            TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            Mapper<MatchEntity, MatchDto> matchMapper,
                            Mapper<PlayerMatchStatEntity, PlayerMatchStatDto> playerMatchStatMapper){
        this.matchRepository = matchRepository;
        this.playerMatchStatRepository = playerMatchStatRepository;
        this.matchMapper = matchMapper;
        this.playerMatchStatMapper = playerMatchStatMapper;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
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
    public List<MatchResultsDto> findAllMatchData() {
        List<MatchResultsDto> matchData = matchRepository.findAllMatchDataWithoutPlayers();
        for (MatchResultsDto match : matchData) {
            Integer matchId = match.getMatchId();
            List<PlayerMatchStatDto> stats = playerMatchStatRepository.findPlayerStatsByMatchId(matchId);

            List<PlayerMatchStatDto> playersA = stats.stream()
                    .filter(p -> p.getTeamId().equals(match.getTeamAId()))
                    .toList();

            List<PlayerMatchStatDto> playersB = stats.stream()
                    .filter(p -> p.getTeamId().equals(match.getTeamBId()))
                    .toList();

            match.setPlayersA(playersA);
            match.setPlayersB(playersB);

            stats.stream()
                    .filter(p -> Boolean.TRUE.equals(p.getPotm()))
                    .findFirst()
                    .ifPresent(p -> match.setPotm(p.getPlayer().getFirstName()));

            stats.stream()
                    .filter(p -> Boolean.TRUE.equals(p.getDotm()))
                    .findFirst()
                    .ifPresent(p -> match.setDotm(p.getPlayer().getFirstName()));
        }
        return matchData;
    }

    @Override
    @Transactional
    public MatchResponseDto importMatch(MatchPayloadDto payload) {
        // create playerEntity if not in playerEntity
        MatchResponseDto responseDto = new MatchResponseDto();
        List<PlayerStatPayloadDto> payloadPlayerMatchStats = payload.getPlayerMatchStats();
        List<MatchPlayerDto> returnedMatchPlayers = new ArrayList<>();

        for(PlayerStatPayloadDto player: payloadPlayerMatchStats){
            String firstName = player.getFirstName();
            String lastName = player.getLastName();
            Optional<PlayerEntity> playerEntity = playerRepository.findByFirstNameAndLastName(firstName, lastName);
            if(!playerEntity.isPresent()) {
                PlayerEntity newPlayer = PlayerEntity
                        .builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .photoUrl("")
                        .position("")
                        .build();
                PlayerEntity savedPlayer = playerRepository.save(newPlayer);
                MatchPlayerDto matchPlayerDto = MatchPlayerDto
                        .builder()
                        .playerId(savedPlayer.getPlayerId())
                        .firstName(savedPlayer.getFirstName())
                        .lastName(savedPlayer.getLastName())
                        .build();
                returnedMatchPlayers.add(matchPlayerDto);

            } else if(playerEntity.isPresent()){
                MatchPlayerDto matchPlayerDto = MatchPlayerDto
                        .builder()
                        .playerId(playerEntity.get().getPlayerId())
                        .firstName(playerEntity.get().getFirstName())
                        .lastName(playerEntity.get().getLastName())
                        .build();
                returnedMatchPlayers.add(matchPlayerDto);
            }



        }
        responseDto.setMatchPlayers(returnedMatchPlayers);

        // create teamEntity
        String captainA = payload.getCaptainA();
        Optional<PlayerEntity> captainEntityA = playerRepository.findByFirstName(captainA);
        TeamEntity teamA = TeamEntity
                .builder()
                .name(payload.getTeamA())
                .captain(captainEntityA.get())
                .build();

        String captainB = payload.getCaptainB();
        Optional<PlayerEntity> captainEntityB = playerRepository.findByFirstName(captainB);
        TeamEntity teamB = TeamEntity.builder()
                .name(payload.getTeamB())
                .captain(captainEntityB.get())
                .build();;

        TeamEntity savedTeamA = teamRepository.save(teamA);
        TeamEntity savedTeamB = teamRepository.save(teamB);

        List<Integer> returnedTeamIds = new ArrayList<>();
        returnedTeamIds.add(savedTeamA.getTeamId());
        returnedTeamIds.add(savedTeamB.getTeamId());

        responseDto.setTeamIds(returnedTeamIds);

        // create matchEntity

        MatchEntity matchEntity = MatchEntity
                .builder()
                .date(payload.getDate())
                .title(payload.getTitle())
                .scoreA(payload.getScoreA())
                .scoreB(payload.getScoreB())
                .location(payload.getLocation())
                .teamA(savedTeamA)
                .teamB(savedTeamB)
                .build();

        MatchEntity savedMatchEntity = matchRepository.save(matchEntity);

        responseDto.setMatchId(savedMatchEntity.getMatchId());

        // create playerMatchStats
        for(PlayerStatPayloadDto player: payloadPlayerMatchStats){
            String firstName = player.getFirstName();
            String lastName = player.getLastName();
            TeamEntity savedTeam;
            PlayerEntity playerEntity = playerRepository.findByFirstNameAndLastName(firstName, lastName).get();
            if (player.getTeam() == savedTeamA.getName()) {
                savedTeam = savedTeamA;
            } else {
                savedTeam = savedTeamB;
            }
            PlayerMatchStatEntity playerMatchStatEntity = PlayerMatchStatEntity
                    .builder()
                    .player(playerEntity)
                    .match(savedMatchEntity)
                    .team(savedTeam)
                    .goals(player.getGoals())
                    .assists(player.getAssists())
                    .won(player.getWin())
                    .potm(player.getPotm())
                    .dotm(player.getDotm())
                    .build();
            playerMatchStatRepository.save(playerMatchStatEntity);
            }


        return responseDto;
    }
}
