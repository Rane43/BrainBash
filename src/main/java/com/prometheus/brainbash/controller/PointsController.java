package com.prometheus.brainbash.controller;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/points")
public class PointsController {
	private IJwtService jwtService;
	private IPointsService pointsService;
	
	public PointsController(IJwtService jwtService, IPointsService pointsService) {
		this.jwtService = jwtService;
		this.pointsService = pointsService;
	}
	
	
	@GetMapping
	@PreAuthorize("hasRole('QUIZZER')")
	public long getPoints(
			@RequestHeader("Authorization") String bearerToken, 
			@RequestParam("quiz_id") long quizId
	) throws UserNotFoundException, QuizNotFoundException {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		return pointsService.getPointsForUserForQuiz(username, quizId);
	}

	@PutMapping
	@PreAuthorize("hasRole('QUIZZER')")
	public long updatePoints(
			@RequestHeader("Authorization") String bearerToken, 
			@Valid @RequestBody PointsUpdateDto pointsUpdateDto
	) throws UserNotFoundException, QuizNotFoundException {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		pointsService.updatePoints(
			username, 
			pointsUpdateDto.getQuizId(), 
			pointsUpdateDto.getPoints()
		);
		return pointsService.getPointsForUserForQuiz(username, pointsUpdateDto.getQuizId());
	}
}
