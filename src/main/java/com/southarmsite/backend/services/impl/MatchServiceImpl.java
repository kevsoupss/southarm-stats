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

import java.time.LocalDate;
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
        Optional<PlayerEntity> playerEntity;
        for(PlayerStatPayloadDto player: payloadPlayerMatchStats){
            String firstName = player.getFirstName();
            firstName = firstName.trim().replaceAll("\\s+", " ");
            String lastName = player.getLastName();
            lastName = lastName.trim().replaceAll("\\s+", " ");

            if (lastName != null && !lastName.isEmpty()) {
                playerEntity = playerRepository.findByFirstNameAndLastName(firstName, lastName);
            } else {
                playerEntity = playerRepository.findByFirstName(firstName);
            }

            if (!playerEntity.isPresent()) {
                PlayerEntity newPlayer = PlayerEntity
                        .builder()
                        .firstName(firstName)
                        .lastName(lastName != null ? lastName : "")
                        .photoUrl("")
                        .positions(List.of(""))
                        .build();
                PlayerEntity savedPlayer = playerRepository.save(newPlayer);
                returnedMatchPlayers.add(MatchPlayerDto.builder()
                        .playerId(savedPlayer.getPlayerId())
                        .firstName(savedPlayer.getFirstName())
                        .lastName(savedPlayer.getLastName())
                        .build());
            } else {
                PlayerEntity existingPlayer = playerEntity.get();
                returnedMatchPlayers.add(MatchPlayerDto.builder()
                        .playerId(existingPlayer.getPlayerId())
                        .firstName(existingPlayer.getFirstName())
                        .lastName(existingPlayer.getLastName())
                        .build());
            }



        }
        responseDto.setMatchPlayers(returnedMatchPlayers);

        // create teamEntity
        String captainA = payload.getCaptainA();
        String[] partsA = captainA.trim().split("\\s+", 2);
        String firstNameA = partsA.length > 0 ? partsA[0].trim().replaceAll("\\s+", " ") : "";
        String lastNameA = partsA.length > 1 ? partsA[1].trim().replaceAll("\\s+", " ") : "";

        Optional<PlayerEntity> captainEntityA;
        if (!lastNameA.isEmpty()) {
            captainEntityA = playerRepository.findByFirstNameAndLastName(firstNameA, lastNameA);
        } else {
            System.err.println("First name fallback: " + firstNameA);
            captainEntityA = playerRepository.findByFirstName(firstNameA);
        }
        if (captainEntityA.isEmpty()) {
            throw new IllegalArgumentException("Captain not found: " + captainA);
        }

        TeamEntity teamA = TeamEntity.builder()
                .name(payload.getTeamA())
                .captain(captainEntityA.get())
                .build();

        String captainB = payload.getCaptainB();
        String[] partsB = captainB.trim().split("\\s+", 2);
        String firstNameB = partsB.length > 0 ? partsB[0].trim().replaceAll("\\s+", " ") : "";
        String lastNameB = partsB.length > 1 ? partsB[1].trim().replaceAll("\\s+", " ") : "";

        Optional<PlayerEntity> captainEntityB;
        if (!lastNameB.isEmpty()) {
            captainEntityB = playerRepository.findByFirstNameAndLastName(firstNameB, lastNameB);
        } else {
            captainEntityB = playerRepository.findByFirstName(firstNameB);
        }
        if (captainEntityB.isEmpty()) {
            throw new IllegalArgumentException("Captain not found: " + captainB);
        }

        TeamEntity teamB = TeamEntity.builder()
                .name(payload.getTeamB())
                .captain(captainEntityB.get())
                .build();

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
        for (PlayerStatPayloadDto player : payloadPlayerMatchStats) {
            String firstName = player.getFirstName();
            firstName = firstName.trim().replaceAll("\\s+", " ");
            String lastName = player.getLastName();
            lastName = lastName.trim().replaceAll("\\s+", " ");

            Optional<PlayerEntity> playerEntityOpt;
            if (lastName != null && !lastName.isEmpty()) {
                playerEntityOpt = playerRepository.findByFirstNameAndLastName(firstName, lastName);
            } else {
                playerEntityOpt = playerRepository.findByFirstName(firstName);
            }

            if (playerEntityOpt.isEmpty()) {
                // Handle player not found, maybe throw or log error
                throw new IllegalArgumentException("Player not found: " + firstName + " " + lastName);
            }

            PlayerEntity playerEntityFound = playerEntityOpt.get();

            TeamEntity savedTeam;
            if (player.getTeam().equals(savedTeamA.getName())) {
                savedTeam = savedTeamA;
            } else {
                savedTeam = savedTeamB;
            }

            PlayerMatchStatEntity playerMatchStatEntity = PlayerMatchStatEntity.builder()
                    .player(playerEntityFound)
                    .match(savedMatchEntity)
                    .team(savedTeam)
                    .goals(player.getGoals())
                    .assists(player.getAssists())
                    .won(player.getWin())
                    .potm(player.getPotm())
                    .dotm(player.getDotm())
                    .ownGoals(player.getOwnGoals())
                    .build();

            playerMatchStatRepository.save(playerMatchStatEntity);
        }


        return responseDto;
    }


    @Override
    public void deleteByDate(LocalDate date) {
        matchRepository.deleteByDate(date);
    }
}
