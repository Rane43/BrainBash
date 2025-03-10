package com.prometheus.brainbash.service.impl;

import org.springframework.stereotype.Service;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserQuizScoreRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.model.UserQuizScore;
import com.prometheus.brainbash.service.IPointsService;

import jakarta.transaction.Transactional;

@Service
public class PointsService implements IPointsService {
	
    private UserQuizScoreRepository userQuizScoreRepo;
    private UserRepository userRepo;
    private QuizRepository quizRepo;
    
    public PointsService(UserQuizScoreRepository userQuizScoreRepo, UserRepository userRepo, QuizRepository quizRepo) {
    	this.userQuizScoreRepo = userQuizScoreRepo;
    	this.userRepo = userRepo;
    	this.quizRepo = quizRepo;
    }
	
    @Override
	@Transactional
	public void updatePoints(String username, Long quizId, int newPoints) throws UserNotFoundException, QuizNotFoundException {
	    // Get the user and quiz exist
		User user = getUser(username);
		Quiz quiz = getQuiz(quizId);
		
		// Find the UserQuizScore for this user and quiz
		UserQuizScore userQuizScore = userQuizScoreRepo.findByUserAndQuiz(user, quiz);
		
		if (userQuizScore == null) {
		   userQuizScore = new UserQuizScore(user, quiz, newPoints);
		} else {
			// Update the points to be the larger of the two 
			int maxPoints = Math.max(newPoints, userQuizScore.getPoints());
			userQuizScore.setPoints(maxPoints);
		}
		
		// Save the updated score
	    userQuizScoreRepo.save(userQuizScore);
	}
    
    @Override
    public int getPointsForUserForQuiz(String username, long quizId) throws UserNotFoundException, QuizNotFoundException{
    	User user = getUser(username);
		Quiz quiz = getQuiz(quizId);

		// Find the UserQuizScore for this user and quiz
		UserQuizScore uqScore = userQuizScoreRepo.findByUserAndQuiz(user, quiz);
		
		// Default to zero if there is no score so far
		return uqScore == null ? 0 : uqScore.getPoints();
    }
    
    
    
    // Helper functions
    private User getUser(String username) throws UserNotFoundException {
    	return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
    
    private Quiz getQuiz(long quizId) throws QuizNotFoundException {
    	return quizRepo.findById(quizId).orElseThrow(() -> new QuizNotFoundException(quizId));
    }
    
   
}
