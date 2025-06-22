package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.entities.MatchEntity;
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

    @Autowired
    public MatchEntityRepositoryIntegrationTest(final MatchRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatCreatesMatch() {
        final MatchEntity matchEntity = createTestMatchA();
        final MatchEntity savedMatchEntity = underTest.save(matchEntity);

        assertThat(savedMatchEntity).isEqualTo(matchEntity);
        final Optional<MatchEntity> result = underTest.findById(matchEntity.getMatchId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(matchEntity);
    }

    @Test
    public void testThatCreateAndFindAllMatches() {
        final MatchEntity matchEntityA = underTest.save(createTestMatchA());
        final MatchEntity matchEntityB = underTest.save(createTestMatchB());

        final List<MatchEntity> expected = List.of(matchEntityA, matchEntityB);
        final List<MatchEntity> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatCreateAndUpdatesMatch() {
        final MatchEntity matchEntityA = underTest.save(createTestMatchA());

        matchEntityA.setDescription("UPDATED DESCRIPTION");
        final MatchEntity updatedMatchEntity = underTest.save(matchEntityA);

        final Optional<MatchEntity> result = underTest.findById(updatedMatchEntity.getMatchId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedMatchEntity);
    }

    @Test
    public void testDeleteMatch() {
        final MatchEntity matchEntityA = underTest.save(createTestMatchA());

        final Optional<MatchEntity> saveResult = underTest.findById(matchEntityA.getMatchId());
        assertThat(saveResult).isPresent();

        underTest.delete(matchEntityA);
        final Optional<MatchEntity> result = underTest.findById(matchEntityA.getMatchId());
        assertThat(result).isNotPresent();

    }
}
