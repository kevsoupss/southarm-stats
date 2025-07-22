package com.southarmsite.backend.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WinrateDto implements Serializable {

    private String name;
    private Double winrate;
    private Long wins;
    private Long losses;
    private Long matches;
    private List<String> positions;

}
