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
@Table(name="goal")
public class GoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goal_id_seq")
    private Integer goalId;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private MatchEntity matchEntity;

    @ManyToOne
    @JoinColumn(name = "scorer_id")
    private PlayerEntity scorer;

    @ManyToOne
    @JoinColumn(name = "assister_id", nullable = true)
    private PlayerEntity assister;

}
