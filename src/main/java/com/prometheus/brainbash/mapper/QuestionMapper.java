package com.prometheus.brainbash.mapper;

import java.util.stream.Collectors;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.model.Question;

public class QuestionMapper {
	
	public static void toQuestionDto(Question question, QuestionDto questionDto) {
		questionDto.setId(question.getId());
		questionDto.setQuizId(question.getQuiz().getId());
		questionDto.setText(question.getText());
		
		questionDto.setAnswerDtos(
			question.getAnswers().stream()
			.map(answer -> {
				AnswerDto answerDto = new AnswerDto();
				AnswerMapper.toAnswerDto(answer, answerDto);
				return answerDto;
			})
			.collect(Collectors.toSet())
		);	
	}
	
	// Private constructor to hide public one
	private QuestionMapper() {}
}
