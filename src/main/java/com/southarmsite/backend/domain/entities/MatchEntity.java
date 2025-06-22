package com.southarmsite.backend.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="match")
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_id_seq")
    private Integer matchId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String location;
    private String description;

}
