package com.prometheus.brainbash.mapper;

import java.util.stream.Collectors;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.dto.QuestionCreationDto;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.model.Answer;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;

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
	
	public static void toQuestion(QuestionCreationDto questionCreationDto, Quiz quiz, Question question) {
		question.setText(questionCreationDto.getText());
		question.setQuiz(quiz);
		question.setAnswers(
				questionCreationDto.getAnswerDtos().stream()
				.map(answerDto -> {
					Answer answer = new Answer();
					AnswerMapper.toAnswer(answerDto, question, answer);
					return answer;
				})
				.collect(Collectors.toSet()));
	}
	
	
	
	// Private constructor to hide public one
	private QuestionMapper() {}
}
