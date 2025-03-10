package com.prometheus.brainbash.mapper;

import java.util.stream.Collectors;

import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;

public final class QuizMapper {
	
	public static void toQuizGameDto(Quiz quiz, QuizGameDto quizGameDto) {
		quizGameDto.setId(quiz.getId());
		quizGameDto.setTitle(quiz.getTitle());
		quizGameDto.setDescription(quiz.getDescription());
		quizGameDto.setImage(quiz.getImage());
		
		quizGameDto.setQuestionIds(quiz.getQuestions()
										.stream()
										.map(Question::getId)
										.collect(Collectors.toSet()));
		
		quizGameDto.setAgeRating(quiz.getAgeRating());
		quizGameDto.setDifficultyRating(quiz.getDifficultyRating());
		quizGameDto.setCategory(quiz.getCategory());
	}
	
	public static void toQuizSummaryDto(Quiz quiz, QuizSummaryDto quizSummaryDto) {
		quizSummaryDto.setId(quiz.getId());
		quizSummaryDto.setTitle(quiz.getTitle());
		quizSummaryDto.setDescription(quiz.getDescription());
		quizSummaryDto.setImage(quiz.getImage());
		quizSummaryDto.setCategory(quiz.getCategory());
	}
}
