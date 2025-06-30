package com.southarmsite.backend.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinStreakDto {
    private String name;
    private Long winStreak;
    private String position;
}