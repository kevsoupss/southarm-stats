package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
}
