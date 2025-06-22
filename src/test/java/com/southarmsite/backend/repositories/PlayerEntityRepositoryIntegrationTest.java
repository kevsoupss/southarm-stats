package com.southarmsite.backend.repositories;

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
public class PlayerEntityRepositoryIntegrationTest {

    private final PlayerRepository underTest;

    @Autowired
    public PlayerEntityRepositoryIntegrationTest(final PlayerRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testCreatePlayerWithId(){
        final PlayerEntity playerEntity = createTestPlayerA();
        final PlayerEntity savedPlayerEntity = underTest.save(playerEntity);

        assertThat(playerEntity).isEqualTo(savedPlayerEntity);
        assertThat(savedPlayerEntity.getPlayerId()).isNotNull();
        assertThat(savedPlayerEntity.getFirstName()).isEqualTo("Kevin");
    }

    @Test
    public void testCreateAndFindPlayerById() {
        final PlayerEntity playerEntity = createTestPlayerB();
        final PlayerEntity savedPlayerEntity = underTest.save(playerEntity);
        final Optional<PlayerEntity> result = underTest.findById(savedPlayerEntity.getPlayerId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedPlayerEntity);
    }

    @Test
    public void testCreateAndFindAllPlayers() {
        final PlayerEntity testPlayerEntityA = underTest.save(createTestPlayerA());
        final PlayerEntity testPlayerEntityB = underTest.save(createTestPlayerB());
        final PlayerEntity testPlayerEntityC = underTest.save(createTestPlayerC());

        final List<PlayerEntity> expected = List.of(testPlayerEntityA, testPlayerEntityB, testPlayerEntityC);
        final List<PlayerEntity> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testCreateUpdatePlayer() {
        final PlayerEntity testPlayerEntityA = underTest.save(createTestPlayerA());
        testPlayerEntityA.setFirstName("UPDATED");
        underTest.save(testPlayerEntityA);
        final Optional<PlayerEntity> result = underTest.findById(testPlayerEntityA.getPlayerId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testPlayerEntityA);

    }

    @Test
    public void testCreateDeletePlayer() {
        final PlayerEntity testPlayerEntityA = underTest.save(createTestPlayerA());
        final Optional<PlayerEntity> saveResult = underTest.findById(testPlayerEntityA.getPlayerId());
        assertThat(saveResult).isPresent();

        underTest.delete(testPlayerEntityA);
        final Optional<PlayerEntity> result = underTest.findById(testPlayerEntityA.getPlayerId());
        assertThat(result).isNotPresent();
    }

}
