package com.prometheus.brainbash.mapper;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.model.Answer;

public class AnswerMapper {
	
	public static void toAnswerDto(Answer answer, AnswerDto answerDto) {
		answerDto.setId(answer.getId());
		answerDto.setText(answer.getText());
		answerDto.setCorrect(answer.isCorrect());
		answerDto.setQuestion_id(answer.getQuestion().getId());
	}
	
	// Private constructor to hide public one
	private AnswerMapper() {}
}
