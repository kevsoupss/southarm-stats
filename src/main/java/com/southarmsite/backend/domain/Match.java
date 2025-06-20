package com.southarmsite.backend.domain;

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
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_id_seq")
    private Integer matchId;
    private LocalDate date;
    private String location;
    private String description;

}
