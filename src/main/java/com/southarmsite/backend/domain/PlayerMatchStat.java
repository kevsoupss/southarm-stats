package com.southarmsite.backend.domain;

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
public class PlayerMatchStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    private int goalsScored;
    private int assists;

    private boolean captain;
}
