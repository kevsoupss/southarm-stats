package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerMatchStatRepository extends JpaRepository<PlayerMatchStatEntity, Integer> {

}
