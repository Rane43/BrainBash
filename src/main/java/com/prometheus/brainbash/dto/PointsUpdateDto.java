package com.prometheus.brainbash.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsUpdateDto {
    private Long quizId;
    private int points;

    
    // Constructors, getters, and setters
    public PointsUpdateDto() {}

    public PointsUpdateDto(Long quizId, int points) {
        this.quizId = quizId;
        this.points = points;
    }
}
