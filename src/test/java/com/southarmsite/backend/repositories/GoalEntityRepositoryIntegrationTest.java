package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.Goal;
import com.southarmsite.backend.domain.Match;
import com.southarmsite.backend.domain.PlayerEntity;
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
public class GoalRepositoryIntegrationTest {
    private final GoalRepository underTest;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public GoalRepositoryIntegrationTest(GoalRepository underTest, PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.underTest = underTest;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;

    }

    @Test
    public void testThatCreateGoal() {
        final Match match = matchRepository.save(createTestMatchA());
        final PlayerEntity scorer = playerRepository.save(createTestPlayerA());
        final PlayerEntity assister = playerRepository.save(createTestPlayerB());
        final Goal goal = createTestGoal(match, scorer, assister);
        final Goal result = underTest.save(goal);

        assertThat(result).isEqualTo(goal);

    }

    @Test
    public void testThatCreateAndFindAllGoals() {
        final Match match = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final Goal goalA = createTestGoal(match, playerEntityA, playerEntityB);
        final Goal goalB = createTestGoal(match, playerEntityB, playerEntityA);
        underTest.save(goalA);
        underTest.save(goalB);

        final List<Goal> expected = List.of(goalA, goalB);
        final List<Goal> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatUpdatedGoal() {
        final Match match = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final Goal goalA = underTest.save(createTestGoal(match, playerEntityA, playerEntityB));

        goalA.setAssister(playerEntityA);
        goalA.setScorer(playerEntityB);

        final Goal updatedGoal = underTest.save(goalA);

        final Optional<Goal> result = underTest.findById(goalA.getGoalId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedGoal);

    }

    @Test
    public void testDeleteGoal() {
        final Match match = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final Goal goalA = underTest.save(createTestGoal(match, playerEntityA, playerEntityB));

        final Optional<Goal> savedResult = underTest.findById(goalA.getGoalId());
        assertThat(savedResult).isPresent();

        underTest.delete(goalA);
        final Optional<Goal> deletedResult = underTest.findById(goalA.getGoalId());
        assertThat(deletedResult).isNotPresent();
    }





}
