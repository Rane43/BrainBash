package com.prometheus.brainbash.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.mapper.QuizMapper;
import com.prometheus.brainbash.model.Quiz;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
	private QuizRepository quizRepo;
	
	public QuizController(QuizRepository quizRepo) {
		this.quizRepo = quizRepo;
	}
	
	@GetMapping
	public List<QuizSummaryDto> getAllQuizzes() {
		return quizRepo.findAll().stream().map((quiz) -> {
			QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
			QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
			return quizSummaryDto;
		}).toList();
	}
	
	@PostMapping
	public void createQuiz(@Valid @RequestBody Quiz quiz) {
		
	}
	
	@GetMapping("/{id}")
	public QuizGameDto getQuizById(@PathVariable long id) throws QuizNotFoundException {
		Optional<Quiz> quizOptional = quizRepo.findById(id);
		
		if (quizOptional.isEmpty()) {
			throw new QuizNotFoundException(id);
		}
		
		QuizGameDto quizGameDto = new QuizGameDto();
		QuizMapper.toQuizGameDto(quizOptional.get(), quizGameDto);
		
		return quizGameDto;
	}
	
	@ExceptionHandler(QuizNotFoundException.class)
	public ResponseEntity<String> handleQuizNotFoundException(QuizNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
