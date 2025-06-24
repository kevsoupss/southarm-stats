package com.southarmsite.backend.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="playerMatchStat")
public class PlayerMatchStatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="player_match_stat_id")
    private Integer playerMatchStatId;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer goals = 0;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer assists = 0;

    @Column(nullable = false)
    private Boolean won;
}
