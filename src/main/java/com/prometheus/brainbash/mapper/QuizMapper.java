package com.prometheus.brainbash.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;

public final class QuizMapper {
	private QuizMapper() {};
	
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
	
	public static void quizCreationDtoToQuiz(QuizCreationDto quizCreationDto, User creator, Quiz quiz) {
		quiz.setTitle(quizCreationDto.getTitle());
		quiz.setDescription(quizCreationDto.getDescription());
		quiz.setImage(quizCreationDto.getImage());
		
		quiz.setCreator(creator);
		// Developers
		Set<User> developers = new HashSet<>();
		developers.add(creator);
		quiz.setDevelopers(developers);
		
		quiz.setQuestions(new HashSet<>());
		
		quiz.setAgeRating(quizCreationDto.getAgeRating());
		quiz.setDifficultyRating(quizCreationDto.getDifficultyRating());
		quiz.setCategory(quizCreationDto.getCategory());
	}
	
}
