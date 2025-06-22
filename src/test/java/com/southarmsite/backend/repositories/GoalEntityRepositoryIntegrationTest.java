package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.GoalEntity;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.southarmsite.backend.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GoalEntityRepositoryIntegrationTest {
    private final GoalRepository underTest;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public GoalEntityRepositoryIntegrationTest(GoalRepository underTest, PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.underTest = underTest;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;

    }

    @Test
    public void testThatCreateGoal() {
        final MatchEntity matchEntity = matchRepository.save(createTestMatchA());
        final PlayerEntity scorer = playerRepository.save(createTestPlayerA());
        final PlayerEntity assister = playerRepository.save(createTestPlayerB());
        final GoalEntity goalEntity = createTestGoal(matchEntity, scorer, assister);
        final GoalEntity result = underTest.save(goalEntity);

        assertThat(result).isEqualTo(goalEntity);

    }

    @Test
    public void testThatCreateAndFindAllGoals() {
        final MatchEntity matchEntity = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final GoalEntity goalEntityA = createTestGoal(matchEntity, playerEntityA, playerEntityB);
        final GoalEntity goalEntityB = createTestGoal(matchEntity, playerEntityB, playerEntityA);
        underTest.save(goalEntityA);
        underTest.save(goalEntityB);

        final List<GoalEntity> expected = List.of(goalEntityA, goalEntityB);
        final List<GoalEntity> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatUpdatedGoal() {
        final MatchEntity matchEntity = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final GoalEntity goalEntityA = underTest.save(createTestGoal(matchEntity, playerEntityA, playerEntityB));

        goalEntityA.setAssister(playerEntityA);
        goalEntityA.setScorer(playerEntityB);

        final GoalEntity updatedGoalEntity = underTest.save(goalEntityA);

        final Optional<GoalEntity> result = underTest.findById(goalEntityA.getGoalId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedGoalEntity);

    }

    @Test
    public void testDeleteGoal() {
        final MatchEntity matchEntity = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final GoalEntity goalEntityA = underTest.save(createTestGoal(matchEntity, playerEntityA, playerEntityB));

        final Optional<GoalEntity> savedResult = underTest.findById(goalEntityA.getGoalId());
        assertThat(savedResult).isPresent();

        underTest.delete(goalEntityA);
        final Optional<GoalEntity> deletedResult = underTest.findById(goalEntityA.getGoalId());
        assertThat(deletedResult).isNotPresent();
    }





}
