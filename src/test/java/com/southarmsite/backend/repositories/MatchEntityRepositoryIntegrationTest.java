package com.southarmsite.backend.repositories;

import com.southarmsite.backend.domain.Match;
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
public class MatchRepositoryIntegrationTest {

    private final MatchRepository underTest;

    @Autowired
    public MatchRepositoryIntegrationTest(final MatchRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatCreatesMatch() {
        final Match match = createTestMatchA();
        final Match savedMatch = underTest.save(match);

        assertThat(savedMatch).isEqualTo(match);
        final Optional<Match> result = underTest.findById(match.getMatchId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(match);
    }

    @Test
    public void testThatCreateAndFindAllMatches() {
        final Match matchA = underTest.save(createTestMatchA());
        final Match matchB = underTest.save(createTestMatchB());

        final List<Match> expected = List.of(matchA, matchB);
        final List<Match> result = underTest.findAll();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testThatCreateAndUpdatesMatch() {
        final Match matchA = underTest.save(createTestMatchA());

        matchA.setDescription("UPDATED DESCRIPTION");
        final Match updatedMatch = underTest.save(matchA);

        final Optional<Match> result = underTest.findById(updatedMatch.getMatchId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedMatch);
    }

    @Test
    public void testDeleteMatch() {
        final Match matchA = underTest.save(createTestMatchA());

        final Optional<Match> saveResult = underTest.findById(matchA.getMatchId());
        assertThat(saveResult).isPresent();

        underTest.delete(matchA);
        final Optional<Match> result = underTest.findById(matchA.getMatchId());
        assertThat(result).isNotPresent();

    }
}
