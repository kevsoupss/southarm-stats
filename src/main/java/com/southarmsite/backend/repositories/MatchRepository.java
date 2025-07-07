package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.dto.MatchResultsDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Integer> {

    List<MatchEntity> findTop5ByOrderByDateDesc();


    @Query("SELECT new com.southarmsite.backend.domain.dto.MatchResultsDto(" +
            "m.matchId, " +
            "m.teamA.name, " +
            "m.teamA.teamId, " +
            "m.teamB.name, " +
            "m.teamB.teamId, " +
            "m.scoreA, " +
            "m.scoreB, " +
            "m.date, " +
            "m.location, " +
            "m.title, " +
            "CONCAT(m.teamA.captain.firstName, ' ', m.teamA.captain.lastName), " +
            "CONCAT(m.teamB.captain.firstName, ' ', m.teamB.captain.lastName), " +
            "null, " +
            "null, " +
            "null, " +
            "null" +
            ") " +
            "FROM MatchEntity m " +
            "ORDER BY m.date DESC"
    )
    List<MatchResultsDto> findAllMatchDataWithoutPlayers();
}
