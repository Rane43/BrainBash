package com.prometheus.brainbash.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
	private long id;
	
	@NotEmpty(message = "Text must be provided")
    private String text;
    
	@NotNull(message = "quizId must be provided")
    private Long quizId;
    
	@NotNull(message = "answerDtos must be provided")
    private Set<AnswerDto> answerDtos;
}
