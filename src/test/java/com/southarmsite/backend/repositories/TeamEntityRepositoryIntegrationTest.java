package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.TeamEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.southarmsite.backend.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeamEntityRepositoryIntegrationTest {
    private final TeamRepository underTest;
    private final PlayerRepository playerRepository;

    @Autowired
    public TeamEntityRepositoryIntegrationTest(TeamRepository underTest, PlayerRepository playerRepository) {
        this.underTest = underTest;
        this.playerRepository = playerRepository;
    }

    @Test
    public void testThatCreateTeam() {
        final PlayerEntity playerA = createTestPlayerA();
        playerRepository.save(playerA);
        final TeamEntity teamEntity = createTestTeamA(playerA);
        final TeamEntity result = underTest.save(teamEntity);

        assertThat(result).isEqualTo(teamEntity);

    }

    @Test
    public void testThatCreateAndFindAllTeams() {

        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final TeamEntity TeamEntityA = createTestTeamA(playerEntityA);
        final TeamEntity TeamEntityB = createTestTeamB(playerEntityB);
        underTest.save(TeamEntityA);
        underTest.save(TeamEntityB);

        final List<TeamEntity> result = underTest.findAll();

        assertThat(result)
                .extracting(TeamEntity::getName)
                .containsExactlyInAnyOrder("Team Kevin", "Team Ronald");
    }

    @Test
    public void testThatUpdatedTeam() {
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final TeamEntity TeamEntityA = underTest.save(createTestTeamA(playerEntityA));

        TeamEntityA.setCaptain(playerEntityB);
        final TeamEntity updatedTeamEntity = underTest.save(TeamEntityA);
        final Optional<TeamEntity> result = underTest.findById(TeamEntityA.getTeamId());
        assertThat(result).isPresent();
        assertThat(result.get().getCaptain().getPlayerId()).isEqualTo(playerEntityB.getPlayerId());

    }

    @Test
    public void testDeleteTeam() {

        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final TeamEntity TeamEntityA = underTest.save(createTestTeamA(playerEntityA));

        final Optional<TeamEntity> savedResult = underTest.findById(TeamEntityA.getTeamId());
        assertThat(savedResult).isPresent();

        underTest.delete(TeamEntityA);
        final Optional<TeamEntity> deletedResult = underTest.findById(TeamEntityA.getTeamId());
        assertThat(deletedResult).isNotPresent();
    }





}
