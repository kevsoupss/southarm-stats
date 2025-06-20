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
@Table(name="goal")
public class Goal {

    @Id
    private Integer goalId;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "scorer_id")
    private Player scorer;

    @ManyToOne
    @JoinColumn(name = "assister_id", nullable = true)
    private Player assister;

}
