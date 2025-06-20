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
@Table(name="player")
public class Player {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "player_id_seq")
    private Integer playerId;

    private String firstName;
    private String lastName;
    private String pictureUrl;
    private String position;

}
