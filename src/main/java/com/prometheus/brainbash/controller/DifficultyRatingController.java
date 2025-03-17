package com.prometheus.brainbash.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.model.DifficultyRating;

@RestController
@RequestMapping("/api/difficulty-ratings")
public class DifficultyRatingController {
	
	@GetMapping
	@PreAuthorize("hasRole('QUIZZER') or hasRole('QUIZ_DESIGNER')")
	public List<DifficultyRating> getAllDifficultyRatings() {
		return List.of(DifficultyRating.values());
	}
}
