package com.southarmsite.backend.repositories;
import com.southarmsite.backend.domain.MatchEntity;
import com.southarmsite.backend.domain.PlayerEntity;
import com.southarmsite.backend.domain.PlayerMatchStatEntity;
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
public class PlayerEntityMatchEntityStatRepositoryIntegrationTest {


    private final PlayerMatchStatRepository underTest;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public PlayerEntityMatchEntityStatRepositoryIntegrationTest(PlayerMatchStatRepository underTest, PlayerRepository playerRepository, MatchRepository matchRepository){
        this.underTest = underTest;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Test
    public void testThatCreatePlayerMatchStat() {
        final MatchEntity matchEntity = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntity = playerRepository.save(createTestPlayerA());
        final PlayerMatchStatEntity playerMatchStatEntity = createTestPlayerMatchStatA(matchEntity, playerEntity);
        final PlayerMatchStatEntity result = underTest.save(playerMatchStatEntity);

        assertThat(result).isEqualTo(playerMatchStatEntity);

    }

    @Test
    public void testThatFindAllPlayerMatchStat() {
        final MatchEntity matchEntityA = matchRepository.save(createTestMatchA());
        final MatchEntity matchEntityB = matchRepository.save(createTestMatchB());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerEntity playerEntityB = playerRepository.save(createTestPlayerB());
        final PlayerMatchStatEntity playerMatchStatEntityA = underTest.save(createTestPlayerMatchStatA(matchEntityA, playerEntityA));
        final PlayerMatchStatEntity playerMatchStatEntityB = underTest.save(createTestPlayerMatchStatB(matchEntityA, playerEntityB));
        final PlayerMatchStatEntity playerMatchStatEntityC = underTest.save(createTestPlayerMatchStatC(matchEntityB, playerEntityA));
        final PlayerMatchStatEntity playerMatchStatEntityD = underTest.save(createTestPlayerMatchStatA(matchEntityB, playerEntityB));

        List<PlayerMatchStatEntity> expected = List.of(playerMatchStatEntityA, playerMatchStatEntityB, playerMatchStatEntityC, playerMatchStatEntityD);

        List<PlayerMatchStatEntity> result = underTest.findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatUpdatesPlayerMatchStat() {
        final MatchEntity matchEntityA = matchRepository.save(createTestMatchA());
        final PlayerEntity playerEntityA = playerRepository.save(createTestPlayerA());
        final PlayerMatchStatEntity playerMatchStatEntityA = underTest.save(createTestPlayerMatchStatA(matchEntityA, playerEntityA));
        Optional<PlayerMatchStatEntity> result = underTest.findById(playerMatchStatEntityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getGoalsScored()).isEqualTo(2);

        playerMatchStatEntityA.setGoalsScored(5);
        underTest.save(playerMatchStatEntityA);

        Optional<PlayerMatchStatEntity> updatedResult = underTest.findById(playerMatchStatEntityA.getId());
        assertThat(updatedResult).isPresent();
        assertThat(updatedResult.get().getGoalsScored()).isEqualTo(5);

    }

}


