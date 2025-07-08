package com.southarmsite.backend.services.impl;

import com.southarmsite.backend.domain.dto.PlayerDto;
import com.southarmsite.backend.domain.dto.PlayerStatsDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.mappers.Mapper;
import com.southarmsite.backend.repositories.PlayerMatchStatRepository;
import com.southarmsite.backend.repositories.PlayerRepository;
import com.southarmsite.backend.repositories.TeamRepository;
import com.southarmsite.backend.services.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;
    private Mapper<PlayerEntity, PlayerDto> playerMapper;
    private final S3Service s3Service;
    private PlayerMatchStatRepository playerMatchStatRepository;
    private TeamRepository teamRepository;

    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            Mapper<PlayerEntity, PlayerDto> playerMapper,
            S3Service s3Service,
            PlayerMatchStatRepository playerMatchStatRepository,
            TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.s3Service = s3Service;
        this.playerMatchStatRepository = playerMatchStatRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        PlayerEntity playerEntity = playerMapper.mapFrom(playerDto);
        PlayerEntity savedPlayerEntity = playerRepository.save(playerEntity);
        return playerMapper.mapTo(savedPlayerEntity);
    }

    @Override
    public List<PlayerDto> findAll() {
        List<PlayerEntity> playerEntityList = StreamSupport.stream(playerRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return playerEntityList.stream().map(playerMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public PlayerDto findByFirstNameAndLastName(String firstName, String lastName) {
        return playerRepository.findByFirstNameAndLastName(firstName, lastName)
                .map(playerMapper::mapTo)
                .orElse(null);
    }

    @Override
    public PlayerDto findByFirstName(String firstName) {
        return playerRepository.findByFirstName(firstName)
                .map(playerMapper::mapTo)
                .orElse(null);  // or throw exception, or return Optional<PlayerDto>
    }

    @Override
    public List<PlayerStatsDto> findAllPlayerStats() {
        List<PlayerStatsDto> playerStatsList = playerRepository.findAllPlayersWithStats();
        for (PlayerStatsDto player : playerStatsList) {
            String s3Key = player.getPhotoUrl();
            if (s3Key != null && !s3Key.isEmpty()) {
                String presignedUrl = s3Service.generatePresignedUrl(s3Key);
                player.setPhotoUrl(presignedUrl);
            }
        }
        return playerStatsList;
    }



    @Override
    public List<PlayerDto> savePlayers(List<PlayerDto> playersPayload) {
        // if player.firstName find is List and is one, replace that, else, if list == 0, add player, if two,return error
        List<PlayerDto> listResponse = new ArrayList<>();
        for(PlayerDto player: playersPayload) {
            // Sanitize input names
            String firstName = player.getFirstName().trim().replaceAll("\\s+", " ");
            String lastName = player.getLastName().trim().replaceAll("\\s+", " ");

            Optional<PlayerEntity> savedPlayer = playerRepository.findByFirstNameAndLastName(firstName, lastName);
            if (savedPlayer.isPresent()) {
                PlayerEntity existing = savedPlayer.get();
                existing.setPhotoUrl(player.getPhotoUrl());
                existing.setPositions(player.getPositions());
                playerRepository.save(existing);
                listResponse.add(playerMapper.mapTo(existing));
            } else {
                List<PlayerEntity> firstNameMatches = playerRepository.findAllByFirstName(firstName);

                if (firstNameMatches.size() == 1) {
                    // if one existing
                    PlayerEntity existing = firstNameMatches.get(0);
                    existing.setLastName(lastName);
                    existing.setPhotoUrl(player.getPhotoUrl());
                    existing.setPositions(player.getPositions());
                    playerRepository.save(existing);
                    listResponse.add(playerMapper.mapTo(existing));
                } else if (firstNameMatches.isEmpty()) {
                    // create new
                    PlayerEntity newPlayer = PlayerEntity.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .photoUrl(player.getPhotoUrl())
                            .positions(player.getPositions())
                            .build();
                    playerRepository.save(newPlayer);
                    listResponse.add(playerMapper.mapTo(newPlayer));
                } else {
                    // multiple first-name matches
                    throw new IllegalArgumentException(
                            "Multiple players found with first name: " + firstName +
                                    ". Please specify a last name."
                    );
                }
            }
        }
        return listResponse;
    }


    @Transactional
    public void mergePlayers(Integer duplicatePlayerId, Integer canonicalPlayerId) {
        playerMatchStatRepository.updatePlayerReferences(duplicatePlayerId, canonicalPlayerId);

        teamRepository.updateCaptainReference(duplicatePlayerId, canonicalPlayerId);

        playerRepository.deleteById(duplicatePlayerId);
    }
}
