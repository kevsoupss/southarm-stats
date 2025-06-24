package com.southarmsite.backend.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "team")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_id_seq")
    @Column(name="team_id")
    private Integer teamId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private PlayerEntity captain;



}
