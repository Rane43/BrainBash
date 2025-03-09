package com.prometheus.brainbash.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.model.Quiz;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
	private QuizRepository quizRepo;
	
	public QuizController(QuizRepository quizRepo) {
		this.quizRepo = quizRepo;
	}
	
	@PostMapping
	public void createQuiz(@Valid @RequestBody Quiz quiz) {
		
	}
}
