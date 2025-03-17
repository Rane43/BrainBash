package com.prometheus.brainbash.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.model.AgeRating;

@RestController
@RequestMapping("/api/age-ratings")
public class AgeRatingController {
	
	@GetMapping
	@PreAuthorize("hasRole('QUIZZER') or hasRole('QUIZ_DESIGNER')")
	public List<AgeRating> getAllAgeRatings() {
		return List.of(AgeRating.values());
	}
}
