package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.dto.*;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
        p.position
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
        p.position
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
        p.position
          
    )
    FROM PlayerEntity p
    LEFT JOIN PlayerMatchStatEntity mp ON mp.player = p
    GROUP BY p.id
    HAVING SUM(CASE WHEN mp.dotm = true THEN 1 ELSE 0 END) > 0
    ORDER BY SUM(CASE WHEN mp.won = true THEN 1 ELSE 0 END)/COUNT(*) DESC
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

    @Query(value = """
    WITH player_streaks AS (
        SELECT 
            p.player_id,
            CONCAT(p.first_name, ' ', p.last_name) as player_name,
            p.position,
            (
                SELECT COUNT(*)
                FROM player_match_stat pms2
                JOIN match m2 ON pms2.match_id = m2.match_id
                WHERE pms2.player_id = p.player_id
                AND pms2.won = true
                AND NOT EXISTS (
                    SELECT 1 FROM player_match_stat pms3
                    JOIN match m3 ON pms3.match_id = m3.match_id
                    WHERE pms3.player_id = p.player_id
                    AND m3.date > m2.date
                    AND pms3.won = false
                )
            ) as current_streak
        FROM player p
        WHERE EXISTS (SELECT 1 FROM player_match_stat pms WHERE pms.player_id = p.player_id)
    )
    SELECT player_name as name, current_streak as winStreak, position
    FROM player_streaks
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
}
