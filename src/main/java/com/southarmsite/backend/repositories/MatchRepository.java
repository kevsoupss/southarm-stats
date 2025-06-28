package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Integer> {

    List<MatchEntity> findTop5ByOrderByDateDesc();

}
