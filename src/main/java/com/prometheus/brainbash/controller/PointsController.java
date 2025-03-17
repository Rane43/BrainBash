package com.prometheus.brainbash.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dto.PointsUpdateDto;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.service.IJwtService;
import com.prometheus.brainbash.service.IPointsService;

@RestController
@RequestMapping("/api/points")
public class PointsController {
	private static final String USER_OR_QUIZ_NOT_FOUND_MESSAGE = "Failed to update points. User or Quiz not found.";
	
	private IJwtService jwtService;
	private IPointsService pointsService;
	
	public PointsController(IJwtService jwtService, IPointsService pointsService) {
		this.jwtService = jwtService;
		this.pointsService = pointsService;
	}
	
	
	@GetMapping
	@PreAuthorize("hasRole('QUIZZER')")
	public ResponseEntity<?> getPoints(@RequestHeader("Authorization") String bearerToken, @RequestParam("quiz_id") long quizId) {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		
		try {
			int points = pointsService.getPointsForUserForQuiz(username, quizId);
			return ResponseEntity.ok().body(points);
		} catch (UserNotFoundException | QuizNotFoundException e) {
			return ResponseEntity.status(400).body(USER_OR_QUIZ_NOT_FOUND_MESSAGE);
		}
	}

	@PutMapping
	@PreAuthorize("hasRole('QUIZZER')")
	public ResponseEntity<?> updatePoints(@RequestHeader("Authorization") String bearerToken, @RequestBody PointsUpdateDto pointsUpdateDto ) {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		try {
			pointsService.updatePoints(
				username, 
				pointsUpdateDto.getQuizId(), 
				pointsUpdateDto.getPoints()
			);
            return ResponseEntity.ok(pointsService.getPointsForUserForQuiz(username, pointsUpdateDto.getQuizId()));
        } catch (UserNotFoundException | QuizNotFoundException e) {
            return ResponseEntity.status(400).body(USER_OR_QUIZ_NOT_FOUND_MESSAGE);
        }
	}
}
