package com.prometheus.brainbash.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
	private long id;
	
    private String text;
    
    private long quizId;
    
    private Set<AnswerDto> answerDtos;
}
