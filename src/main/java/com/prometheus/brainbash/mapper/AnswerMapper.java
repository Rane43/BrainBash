package com.prometheus.brainbash.mapper;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.model.Answer;
import com.prometheus.brainbash.model.Question;

public class AnswerMapper {
	
	public static void toAnswerDto(Answer answer, AnswerDto answerDto) {
		answerDto.setId(answer.getId());
		answerDto.setText(answer.getText());
		answerDto.setCorrect(answer.isCorrect());
		answerDto.setQuestion_id(answer.getQuestion().getId());
	}
	
	public static void toAnswer(AnswerDto answerDto, Question question, Answer answer) {
		answer.setText(answerDto.getText());
		answer.setQuestion(question);
		answer.setCorrect(answerDto.isCorrect());
	}
	
	// Private constructor to hide public one
	private AnswerMapper() {}
}
