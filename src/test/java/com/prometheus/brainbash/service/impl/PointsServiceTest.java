package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserQuizScoreRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.model.UserQuizScore;

class PointsServiceTest {
	private UserRepository userRepo;
	private QuizRepository quizRepo;
	private UserQuizScoreRepository pointsRepo;
	
	private PointsService pointsService;
	
	// Example User
	private User user;
	private String username = "newUser";
	
	// Example Quiz
	private Quiz quiz;
	private long quizId = 1L;
	
	
	@BeforeEach
	public void setup() {
		// Example User
		user = new User();
		user.setId(1L);
		user.setUsername(username);
		user.setPassword("ahajsdg(HASHED-PASSWORD)JASDLFK");
		user.setRole(Role.ROLE_QUIZZER);
		
		// Example Quiz
		quiz = new Quiz();
		quiz.setId(quizId);
		
		
		// Mocks
		userRepo = mock(UserRepository.class);
		quizRepo = mock(QuizRepository.class);
		pointsRepo = mock(UserQuizScoreRepository.class);
		
		pointsService = new PointsService(pointsRepo, userRepo, quizRepo);
		
	}
	
	// ---------------- Update points -------------------
	@Test
	void testCorrectlyUpdatesPoints() {
		final int points = 5;
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(null);
		
		pointsService.updatePoints(username, quizId, points);
		
		verify(pointsRepo, new Times(1)).save(any());
	}
	
	@Test
	void testUserNotFoundWhileUpdatingPoints() {
		final int points = 5;
		when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
		when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(null);
		
		Throwable e = assertThrows(UserNotFoundException.class, () -> {
			pointsService.updatePoints(username, quizId, points);
		});
		assertEquals("User with username: " + username + ", cannot be found.", e.getMessage());
		
		verify(pointsRepo, new Times(0)).save(any());
	}
	
	@Test
	void testQuizNotFoundWhileUpdatingPoints() {
		final int points = 5;
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.empty());
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(null);
		
		Throwable e = assertThrows(QuizNotFoundException.class, () -> {
			pointsService.updatePoints(username, quizId, points);
		});
		assertEquals("Quiz with id: " + quizId + ", cannot be found.", e.getMessage());
		
		verify(pointsRepo, new Times(0)).save(any());
	}
	
	@Test
	void testHighestScoreAlreadyExists() {
		final int points = 5;
		final int olderPoints = 3;
		
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
		
		UserQuizScore pointsPrev = new UserQuizScore();
		pointsPrev.setId(1L);
		pointsPrev.setPoints(olderPoints);
		pointsPrev.setQuiz(quiz);
		pointsPrev.setUser(user);
		
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(pointsPrev);
		
		pointsService.updatePoints(username, quizId, points);
		
		verify(pointsRepo, new Times(1)).save(any());
	}
	
	
	// ------------------ Get Points ----------------------
	@Test
	void testCorrectlyGetsPoints() {
		final int points = 5;
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
		
		UserQuizScore pointsObj = new UserQuizScore();
		pointsObj.setId(1L);
		pointsObj.setPoints(points);
		pointsObj.setQuiz(quiz);
		pointsObj.setUser(user);
		
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(pointsObj);
		
		int result = pointsService.getPointsForUserForQuiz(username, quizId);
		
		assertEquals(points, result);

	}
	
	@Test
	void testDefaultIsZero() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(null);
		
		int result = pointsService.getPointsForUserForQuiz(username, quizId);
		
		assertEquals(0, result);
	}
	
	@Test
	void testUserNotFoundWhileGettingPoints() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.empty());
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(null);
		
		Throwable e = assertThrows(QuizNotFoundException.class, () -> {
			pointsService.getPointsForUserForQuiz(username, quizId);
		});
		assertEquals("Quiz with id: " + quizId + ", cannot be found.", e.getMessage());
	}
	
	@Test
	void testQuizNotFoundWhileGettingPoints() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(quizRepo.findById(quizId)).thenReturn(Optional.empty());
		when(pointsRepo.findByUserAndQuiz(user, quiz)).thenReturn(null);
		
		Throwable e = assertThrows(QuizNotFoundException.class, () -> {
			pointsService.getPointsForUserForQuiz(username, quizId);
		});
		assertEquals("Quiz with id: " + quizId + ", cannot be found.", e.getMessage());
	}

}
