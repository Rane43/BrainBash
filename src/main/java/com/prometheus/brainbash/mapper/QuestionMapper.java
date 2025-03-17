package com.prometheus.brainbash.mapper;

import java.util.stream.Collectors;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.dto.QuestionRequestDto;
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
	
	public static void toQuestion(QuestionRequestDto questionRequestDto, Quiz quiz, Question question) {
		question.setText(questionRequestDto.getText());
		question.setQuiz(quiz);
		question.getAnswers().clear();
		questionRequestDto.getAnswerRequestDtos().stream()
		.map(answerReqDto -> {
			Answer answer = new Answer();
			AnswerMapper.toAnswer(answerReqDto, question, answer);
			return answer;
		}).forEach((answer) -> {
			question.getAnswers().add(answer);
		});;
	}
	
	// Private constructor to hide public one
	private QuestionMapper() {}
}
