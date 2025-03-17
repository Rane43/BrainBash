package com.prometheus.brainbash.dto;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestionRequestDto {
	@NotEmpty(message = "Question text cannot be empty.")
    private String text;
    
	@NotNull(message = "Answer Dtos cannot be null.")
	@Size(min=3, max=3, message = "There must be exactly 3 answers")
    private Set<AnswerRequestDto> answerRequestDtos;
}
