package com.southarmsite.backend.repositories;
import com.southarmsite.backend.domain.entities.MatchEntity;
import com.southarmsite.backend.domain.entities.PlayerEntity;
import com.southarmsite.backend.domain.entities.PlayerMatchStatEntity;
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
public class PlayerMatchStatEntityRepositoryIntegrationTest {


    private final PlayerMatchStatRepository underTest;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerMatchStatEntityRepositoryIntegrationTest(PlayerMatchStatRepository underTest, PlayerRepository playerRepository, MatchRepository matchRepository, TeamRepository teamRepository){
        this.underTest = underTest;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    @Test
    public void testThatCreatePlayerMatchStat() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));


        final MatchEntity matchEntity = matchRepository.save(createTestMatchA(teamA, teamB));

        final PlayerMatchStatEntity playerMatchStatEntity = createTestPlayerMatchStatA(matchEntity, playerA, teamA);
        final PlayerMatchStatEntity result = underTest.save(playerMatchStatEntity);

        assertThat(result).isEqualTo(playerMatchStatEntity);

    }

    @Test
    public void testThatFindAllPlayerMatchStat() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));


        final MatchEntity matchEntity = matchRepository.save(createTestMatchA(teamA, teamB));

        final PlayerMatchStatEntity playerMatchStatEntityA = underTest.save(createTestPlayerMatchStatA(matchEntity, playerA, teamA));
        final PlayerMatchStatEntity playerMatchStatEntityB = underTest.save(createTestPlayerMatchStatB(matchEntity, playerA, teamA));

        final List<PlayerMatchStatEntity> expected = List.of(playerMatchStatEntityA, playerMatchStatEntityB);
        final List<PlayerMatchStatEntity> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void testThatUpdatesPlayerMatchStat() {
        final PlayerEntity playerA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerB = playerRepository.save(createTestPlayerB());
        final TeamEntity teamA = teamRepository.save(createTestTeamA(playerA));
        final TeamEntity teamB = teamRepository.save(createTestTeamB(playerB));


        final MatchEntity matchEntity = matchRepository.save(createTestMatchA(teamA, teamB));
        final PlayerMatchStatEntity playerMatchStatEntity = createTestPlayerMatchStatA(matchEntity, playerA, teamA);
        final PlayerMatchStatEntity savedPlayerMatchStatEntity = underTest.save(playerMatchStatEntity);

        savedPlayerMatchStatEntity.setGoals(5);
        underTest.save(savedPlayerMatchStatEntity);
        final Optional<PlayerMatchStatEntity> result = underTest.findById(savedPlayerMatchStatEntity.getPlayerMatchStatId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedPlayerMatchStatEntity);

    }

}


