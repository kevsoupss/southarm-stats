package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Integer> {
}
