package com.southarmsite.backend;


import com.southarmsite.backend.domain.Goal;
import com.southarmsite.backend.domain.Match;
import com.southarmsite.backend.domain.Player;
import com.southarmsite.backend.domain.PlayerMatchStat;
import jakarta.persistence.*;

import java.time.LocalDate;

public final class TestDataUtil {
    private TestDataUtil() {
        throw new UnsupportedOperationException("Utility classes cannot be constructed");
    }

    public static Player createTestPlayerA() {
        return Player.builder()
                .firstName("Kevin")
                .lastName("Lei")
                .position("Winger")
                .pictureUrl("testUrl")
                .build();
    }

    public static Player createTestPlayerB() {
        return Player.builder()
                .firstName("Ronald")
                .lastName("Lam")
                .position("Winger")
                .pictureUrl("testUrl2")
                .build();
    }

    public static Player createTestPlayerC() {
        return Player.builder()
                .firstName("Rickey")
                .lastName("Lam")
                .position("Striker")
                .pictureUrl("testUrl3")
                .build();
    }


    public static Player createTestPlayerD() {
        return Player.builder()
                .firstName("Michael")
                .lastName("Jeff")
                .position("Center Midfield")
                .pictureUrl("testUrl4")
                .build();
    }

    public static Match createTestMatchA() {
        return Match.builder()
                .date(LocalDate.of(2025, 5, 20))
                .location("Southarm")
                .description("Jeff v Michael Special")
                .build();
    }

    public static Match createTestMatchB() {
        return Match.builder()
                .date(LocalDate.of(2025, 5, 27))
                .location("King George")
                .description("Kevin v Ronald Bday Special")
                .build();
    }


    public static Goal createTestGoal(final Match match, final Player scorer, final Player assister) {
        return Goal.builder()
                .match(match)
                .scorer(scorer)
                .assister(assister)
                .build();
    }


    public static PlayerMatchStat createTestPlayerMatchStatA(final Match match, final Player player) {
        return PlayerMatchStat.builder()
                .match(match)
                .player(player)
                .goalsScored(2)
                .assists(1)
                .captain(false)
                .build();
    }

    public static PlayerMatchStat createTestPlayerMatchStatB(final Match match, final Player player) {
        return PlayerMatchStat.builder()
                .match(match)
                .player(player)
                .goalsScored(3)
                .assists(3)
                .captain(true)
                .build();
    }

    public static PlayerMatchStat createTestPlayerMatchStatC(final Match match, final Player player) {
        return PlayerMatchStat.builder()
                .match(match)
                .player(player)
                .goalsScored(2)
                .assists(1)
                .captain(true)
                .build();
    }


}
