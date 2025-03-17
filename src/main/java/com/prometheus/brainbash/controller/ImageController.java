package com.prometheus.brainbash.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.service.IQuizImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
	private IQuizImageService quizImageService;
	
	public ImageController(IQuizImageService quizImageService) {
		this.quizImageService = quizImageService;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('QUIZZER') or hasRole('QUIZ_DESIGNER')")
	public ResponseEntity<List<String>> getAllQuizImages() {
		return ResponseEntity.ok().body(quizImageService.findAll());
	}
}
