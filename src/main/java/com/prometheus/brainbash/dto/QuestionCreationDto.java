package com.prometheus.brainbash.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestionCreationDto {
	@NotEmpty(message = "Question text cannot be empty.")
    private String text;
    
	@NotNull(message = "Answer Dtos cannot be null.")
    private Set<AnswerDto> answerDtos;
}
