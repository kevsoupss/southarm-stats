package com.southarmsite.backend.repositories;
import com.southarmsite.backend.domain.Goal;
import com.southarmsite.backend.domain.Match;
import com.southarmsite.backend.domain.Player;
import com.southarmsite.backend.domain.PlayerMatchStat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.southarmsite.backend.TestDataUtil.*;
import static com.southarmsite.backend.TestDataUtil.createTestGoal;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerMatchStatRepositoryTest {


    private final PlayerMatchStatRepository underTest;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public PlayerMatchStatRepositoryTest(PlayerMatchStatRepository underTest, PlayerRepository playerRepository, MatchRepository matchRepository){
        this.underTest = underTest;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Test
    public void testThatCreatePlayerMatchStat() {
        final Match match = matchRepository.save(createTestMatchA());
        final Player player = playerRepository.save(createTestPlayerA());
        final PlayerMatchStat playerMatchStat = createTestPlayerMatchStatA(match, player);
        final PlayerMatchStat result = underTest.save(playerMatchStat);

        assertThat(result).isEqualTo(playerMatchStat);

    }

    @Test
    public void testThatFindAllPlayerMatchStat() {
        final Match matchA = matchRepository.save(createTestMatchA());
        final Match matchB = matchRepository.save(createTestMatchB());
        final Player playerA = playerRepository.save(createTestPlayerA());
        final Player playerB = playerRepository.save(createTestPlayerB());
        final PlayerMatchStat playerMatchStatA = underTest.save(createTestPlayerMatchStatA(matchA, playerA));
        final PlayerMatchStat playerMatchStatB = underTest.save(createTestPlayerMatchStatB(matchA, playerB));
        final PlayerMatchStat playerMatchStatC = underTest.save(createTestPlayerMatchStatC(matchB, playerA));
        final PlayerMatchStat playerMatchStatD = underTest.save(createTestPlayerMatchStatA(matchB, playerB));

        List<PlayerMatchStat> expected = List.of(playerMatchStatA, playerMatchStatB, playerMatchStatC, playerMatchStatD);

        List<PlayerMatchStat> result = underTest.findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatUpdatesPlayerMatchStat() {
        final Match matchA = matchRepository.save(createTestMatchA());
        final Player playerA = playerRepository.save(createTestPlayerA());
        final PlayerMatchStat playerMatchStatA = underTest.save(createTestPlayerMatchStatA(matchA, playerA));
        Optional<PlayerMatchStat> result = underTest.findById(playerMatchStatA.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getGoalsScored()).isEqualTo(2);

        playerMatchStatA.setGoalsScored(5);
        underTest.save(playerMatchStatA);

        Optional<PlayerMatchStat> updatedResult = underTest.findById(playerMatchStatA.getId());
        assertThat(updatedResult).isPresent();
        assertThat(updatedResult.get().getGoalsScored()).isEqualTo(5);

    }

}


