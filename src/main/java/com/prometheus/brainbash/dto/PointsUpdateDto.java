package com.prometheus.brainbash.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointsUpdateDto {
	@NotNull(message = "quizId must be supplied")
    private Long quizId;
    private int points;

    
    // Constructors, getters, and setters
    public PointsUpdateDto() {}

    public PointsUpdateDto(Long quizId, int points) {
        this.quizId = quizId;
        this.points = points;
    }
}
