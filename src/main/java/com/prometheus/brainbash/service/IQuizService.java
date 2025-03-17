package com.prometheus.brainbash.service;

import java.util.List;

import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.DifficultyRating;

public interface IQuizService {
	/* ------------------- CRUD QUIZZES -------------------------- */
	public List<QuizSummaryDto> findAll();
	
	public QuizGameDto findById(long id) throws QuizNotFoundException;
	
	public long save(String bearerToken, QuizCreationDto quizCreationDto) throws UserNotFoundException;
	
	public List<QuizSummaryDto> findBySearch(
			String middleTitle, 
	        DifficultyRating difficultyRating,
	        AgeRating ageRating);
	
	public List<QuizSummaryDto> findMineBySearch(
			String bearerToken, 
			String middleTitle, 
			DifficultyRating difficultyRating, 
			AgeRating ageRating) throws UnauthorizedAccessToQuizException, UserNotFoundException;
	
	public void delete(String bearerToken, long quizId) throws UnauthorizedAccessToQuizException, UserNotFoundException, QuizNotFoundException;
}
