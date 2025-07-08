package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {

    @Modifying
    @Query("UPDATE TeamEntity t SET t.captain.playerId = :canonicalId WHERE t.captain.playerId = :duplicateId")
    int updateCaptainReference(@Param("duplicateId") Integer duplicateId, @Param("canonicalId") Integer canonicalId);
}

