package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.dto.DOTMDto;
import com.southarmsite.backend.domain.dto.POTMDto;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}
