package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMatchStatRepository extends JpaRepository<PlayerMatchStatEntity, Integer> {

    List<PlayerMatchStatEntity> findByMatch(MatchEntity match);

}
