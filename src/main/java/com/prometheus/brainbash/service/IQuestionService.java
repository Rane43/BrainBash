package com.prometheus.brainbash.service;

import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.dto.QuestionRequestDto;
import com.prometheus.brainbash.exception.QuestionNotFoundException;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;

public interface IQuestionService {
	
	public QuestionDto findById(long id) throws QuestionNotFoundException;
	
	public long createQuestion(
			String bearerToken, 
			long quizId, 
			QuestionRequestDto questionCreationDto
	) throws UserNotFoundException, QuizNotFoundException, UnauthorizedAccessToQuizException;
	
	public void updateQuestion(
			String bearerToken, 
			long questionId,
			QuestionRequestDto questionDto
	) throws UserNotFoundException, QuestionNotFoundException, UnauthorizedAccessToQuizException;
	
	public void delete(String bearerToken, long questionId) throws UserNotFoundException, QuestionNotFoundException, UnauthorizedAccessToQuizException;;
}
