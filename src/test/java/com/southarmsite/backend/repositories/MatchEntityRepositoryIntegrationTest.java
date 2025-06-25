package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
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
public class MatchEntityRepositoryIntegrationTest {

    private final MatchRepository underTest;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public MatchEntityRepositoryIntegrationTest(final MatchRepository underTest, final TeamRepository teamRepository, final PlayerRepository playerRepository) {
        this.underTest = underTest;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Test
    public void testThatCreatesMatch() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));
        final MatchEntity matchEntity = createTestMatchA(teamA, teamB);
        final MatchEntity result = underTest.save(matchEntity);

        assertThat(result).isEqualTo(matchEntity);
    }

    @Test
    public void testThatCreateAndFindAllMatches() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));
        final MatchEntity matchEntityA = underTest.save(createTestMatchA(teamA, teamB));
        final MatchEntity matchEntityB = underTest.save(createTestMatchB(teamB, teamA));

        final List<MatchEntity> expected = List.of(matchEntityA, matchEntityB);
        final List<MatchEntity> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatCreateAndUpdatesMatch() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));

        final MatchEntity savedMatchEntity = underTest.save(createTestMatchA(teamA, teamB));
        savedMatchEntity.setLocation("UPDATED");
        final MatchEntity updatedMatchEntity = underTest.save(savedMatchEntity);

        final Optional<MatchEntity> result = underTest.findById(updatedMatchEntity.getMatchId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedMatchEntity);
    }

    @Test
    public void testDeleteMatch() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));

        final MatchEntity savedMatchEntity = underTest.save(createTestMatchA(teamA, teamB));

        final Optional<MatchEntity> saveResult = underTest.findById(savedMatchEntity.getMatchId());
        assertThat(saveResult).isPresent();

        underTest.delete(savedMatchEntity);
        final Optional<MatchEntity> result = underTest.findById(savedMatchEntity.getMatchId());
        assertThat(result).isNotPresent();

    }
}
