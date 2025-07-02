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
@Table(name = "team")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="team_id")
    private Integer teamId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private PlayerEntity captain;



}
