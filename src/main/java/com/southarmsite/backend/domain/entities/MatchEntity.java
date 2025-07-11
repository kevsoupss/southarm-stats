package com.southarmsite.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="match")
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="match_id")
    private Integer matchId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "team_a_id", nullable = false)
    private TeamEntity teamA;

    @OneToOne
    @JoinColumn(name = "team_b_id", nullable = false)
    private TeamEntity teamB;

    @Column(name = "score_a", nullable = false)
    private Integer scoreA;

    @Column(name = "score_b", nullable = false)
    private Integer scoreB;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String title;

}
