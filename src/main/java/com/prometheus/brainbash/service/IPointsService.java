package com.prometheus.brainbash.service;

import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UserNotFoundException;

public interface IPointsService {
	void updatePoints(String username, Long quizId, int newPoints) throws UserNotFoundException, QuizNotFoundException;
	
	int getPointsForUserForQuiz(String username, long quiz_id) throws UserNotFoundException, QuizNotFoundException;
}
