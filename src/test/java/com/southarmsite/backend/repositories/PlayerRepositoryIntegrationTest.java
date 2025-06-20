package com.southarmsite.backend.repositories;

import com.southarmsite.backend.TestDataUtil;
import com.southarmsite.backend.domain.Player;
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
public class PlayerRepositoryIntegrationTest {

    private final PlayerRepository underTest;

    @Autowired
    public PlayerRepositoryIntegrationTest(final PlayerRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testCreatePlayerWithId(){
        final Player player = createTestPlayerA();
        final Player savedPlayer = underTest.save(player);

        assertThat(player).isEqualTo(savedPlayer);
        assertThat(savedPlayer.getPlayerId()).isNotNull();
        assertThat(savedPlayer.getFirstName()).isEqualTo("Kevin");
    }

    @Test
    public void testCreateAndFindPlayerById() {
        final Player player = createTestPlayerB();
        final Player savedPlayer = underTest.save(player);
        final Optional<Player> result = underTest.findById(savedPlayer.getPlayerId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedPlayer);
    }

    @Test
    public void testCreateAndFindAllPlayers() {
        final Player testPlayerA = underTest.save(createTestPlayerA());
        final Player testPlayerB = underTest.save(createTestPlayerB());
        final Player testPlayerC = underTest.save(createTestPlayerC());

        final List<Player> expected = List.of(testPlayerA, testPlayerB, testPlayerC);
        final List<Player> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCreateUpdatePlayer() {
        final Player testPlayerA = underTest.save(createTestPlayerA());
        testPlayerA.setFirstName("UPDATED");
        underTest.save(testPlayerA);
        final Optional<Player> result = underTest.findById(testPlayerA.getPlayerId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testPlayerA);

    }

    @Test
    public void testCreateDeletePlayer() {
        final Player testPlayerA = underTest.save(createTestPlayerA());
        final Optional<Player> saveResult = underTest.findById(testPlayerA.getPlayerId());
        assertThat(saveResult).isPresent();

        underTest.delete(testPlayerA);
        final Optional<Player> result = underTest.findById(testPlayerA.getPlayerId());
        assertThat(result).isNotPresent();
    }

}
