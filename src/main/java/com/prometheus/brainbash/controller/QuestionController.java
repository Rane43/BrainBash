package com.prometheus.brainbash.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dao.QuestionRepository;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.exception.QuestionNotFoundException;
import com.prometheus.brainbash.mapper.QuestionMapper;
import com.prometheus.brainbash.model.Question;


@RestController
@RequestMapping("/api/questions")
public class QuestionController {
	private QuestionRepository questionRepo;

	public QuestionController(QuestionRepository questionRepo) {
		this.questionRepo = questionRepo;
	}
	
	@GetMapping("/{id}")
	public QuestionDto getQuestionById(@PathVariable long id) throws QuestionNotFoundException {
		Optional<Question> questionOptional = questionRepo.findById(id);
		
		if (questionOptional.isEmpty()) {
			throw new QuestionNotFoundException("Question with id '" + id + "' cannot be found.");
		}
		
		QuestionDto questionDto = new QuestionDto();
		QuestionMapper.toQuestionDto(questionOptional.get(), questionDto);
		
		return questionDto;
	}
	

	// ----------------------- QUESTION HANDLER -------------------------
	@ExceptionHandler(QuestionNotFoundException.class)
	public ResponseEntity<String> handleQuizNotFoundException(QuestionNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
