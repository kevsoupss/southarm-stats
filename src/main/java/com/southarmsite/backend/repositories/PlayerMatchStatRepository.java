package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMatchStatRepository extends JpaRepository<PlayerMatchStatEntity, Integer> {

    List<PlayerMatchStatEntity> findByMatch(MatchEntity match);

    @Query("""
    SELECT new com.southarmsite.backend.domain.dto.POTMDto(
        CONCAT(p.firstName, ' ', p.lastName),
        COALESCE(SUM(CASE WHEN mp.potm = true THEN 1 ELSE 0 END), 0),
        p.positions
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.id
    HAVING SUM(CASE WHEN mp.potm = true THEN 1 ELSE 0 END) > 0
    ORDER BY SUM(CASE WHEN mp.potm = true THEN 1 ELSE 0 END) DESC
    """)
    List<POTMDto> findTopPOTM();

    @Query("""
    SELECT new com.southarmsite.backend.domain.dto.DOTMDto(
        CONCAT(p.firstName, ' ', p.lastName),
        COALESCE(SUM(CASE WHEN mp.dotm = true THEN 1 ELSE 0 END), 0),
        p.positions
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.id
    HAVING SUM(CASE WHEN mp.dotm = true THEN 1 ELSE 0 END) > 0
    ORDER BY SUM(CASE WHEN mp.dotm = true THEN 1 ELSE 0 END) DESC
    """)
    List<DOTMDto> findTopDOTM();

    @Query("""
    SELECT new com.southarmsite.backend.domain.dto.WinrateDto(
        CONCAT(p.firstName, ' ', p.lastName),
        COALESCE(SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END) * 1.0 / COUNT(*), 0.0),
        COALESCE(SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN mp.won = false THEN 1 ELSE 0 END), 0),
        COALESCE(COUNT(*), 0),
        p.positions
          
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.id
    HAVING SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END) > 0
    ORDER BY\s
        (SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END) * 1.0 / COUNT(*)) *\s
        POWER(COUNT(*), 0.3) DESC
    LIMIT 10
    """)
    List<WinrateDto> findTopWinrate();

    @Query("""
    SELECT new com.southarmsite.backend.domain.dto.ScorerDto(
        CONCAT(p.firstName, ' ', p.lastName),
        COALESCE(SUM(mp.goals), 0),
        COALESCE(SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN mp.won = false THEN 1 ELSE 0 END), 0)
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.id
    HAVING SUM(mp.goals) > 0
    ORDER BY SUM(mp.goals) DESC
    """)
    List<ScorerDto> findTopScorer();

    @Query("""
    SELECT new com.southarmsite.backend.domain.dto.AssisterDto(
        CONCAT(p.firstName, ' ', p.lastName),
        COALESCE(SUM(mp.assists), 0),
        COALESCE(SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN mp.won = false THEN 1 ELSE 0 END), 0)
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.id
    HAVING SUM(mp.assists) > 0
    ORDER BY SUM(mp.assists) DESC
    """)
    List<ScorerDto> findTopAssisters();

    @Query(value = """
        WITH player_matches AS (
                   SELECT 
                       p.player_id,
                       CONCAT(p.first_name, ' ', p.last_name) as player_name,
                       p.positions,
                       pms.won,
                       m.date,
                       ROW_NUMBER() OVER(PARTITION BY p.player_id ORDER BY m.date DESC) as match_rank
                   FROM player p
                   JOIN player_match_stat pms ON p.player_id = pms.player_id
                   JOIN match m ON pms.match_id = m.match_id
               ),
               streaks AS (
                   SELECT 
                       player_id,
                       player_name,
                       positions,
                       won,
                       match_rank,
                       ROW_NUMBER() OVER(PARTITION BY player_id ORDER BY match_rank) - 
                       ROW_NUMBER() OVER(PARTITION BY player_id, won ORDER BY match_rank) as streak_group
                   FROM player_matches
               ),
               current_streaks AS (
                   SELECT 
                       player_id,
                       player_name,
                       positions,
                       CASE 
                           WHEN won = true AND MIN(match_rank) = 1 THEN COUNT(*)
                           ELSE 0
                       END as current_streak
                   FROM streaks
                   WHERE streak_group = (
                       SELECT MIN(s2.streak_group)
                       FROM streaks s2 
                       WHERE s2.player_id = streaks.player_id
                   )
                   GROUP BY player_id, player_name, positions, won
               )
               SELECT 
                   player_name as name, 
                   current_streak as winStreak, 
                   array_to_string(positions, ',') as positions
               FROM current_streaks
               WHERE current_streak > 0
               ORDER BY current_streak DESC
               LIMIT 5
""", nativeQuery = true)
    List<WinStreakDto> getTop5WinStreakers();


    @Query("""
        SELECT new com.southarmsite.backend.domain.dto.PlayerMatchStatDto(
            p.id,
            new com.southarmsite.backend.domain.dto.MatchPlayerDto(
                pl.id,
                pl.firstName,
                pl.lastName
            ),
            p.goals,
            p.assists,
            p.ownGoals,
            p.won,
            p.match.id,
            p.team.id,
            p.potm,
            p.dotm
        )
        FROM PlayerMatchStatEntity p
        JOIN p.player pl
        WHERE p.match.id = :matchId
    """)
    List<PlayerMatchStatDto> findPlayerStatsByMatchId(@Param("matchId") Integer matchId);

    @Modifying
    @Query("UPDATE PlayerMatchStatEntity mp SET mp.player.playerId = :canonicalId WHERE mp.player.playerId = :duplicateId")
    int updatePlayerReferences(@Param("duplicateId") Integer duplicateId, @Param("canonicalId") Integer canonicalId);
}
