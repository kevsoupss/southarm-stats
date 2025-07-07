package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.dto.PlayerMatchStatDto;
import com.southarmsite.backend.domain.dto.PlayerStatsDto;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Integer> {

    @Query("""
    SELECT new com.southarmsite.backend.domain.dto.PlayerStatsDto(
        p.playerId,
        p.firstName,
        p.lastName,
        p.positions,
        p.photoUrl,
        COALESCE(SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN mp.won = false THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(mp.goals), 0),
        COALESCE(SUM(mp.assists), 0),
        COUNT(mp.id),
        COALESCE(SUM(CASE WHEN mp.potm = true THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN mp.dotm = true THEN 1 ELSE 0 END), 0)
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.playerId, p.firstName, p.lastName, p.positions, p.photoUrl
    ORDER BY p.firstName, p.lastName""")
    List<PlayerStatsDto> findAllPlayersWithStats();


    Optional<PlayerEntity> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<PlayerEntity> findByFirstName(String firstName);

    List<PlayerEntity> findAllByFirstName(String firstName);





}
