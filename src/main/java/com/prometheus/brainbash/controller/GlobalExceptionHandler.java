package com.prometheus.brainbash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prometheus.brainbash.exception.QuestionNotFoundException;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// ------------------- VALIDATION EXCEPTION HANDLER ------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        // Extract field-specific errors
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Return a 400 Bad Request with error details
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    
    
    // ------------------- QUESTION EXCEPTION HANDLERS ------------------------
 	@ExceptionHandler(QuestionNotFoundException.class)
 	public ResponseEntity<String> handleQuizNotFoundException(QuestionNotFoundException e) {
 		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
 	}
 	
 	
 	
 	// --------------------- QUIZ EXCEPTION HANDLERS --------------------------
 	@ExceptionHandler(UnauthorizedAccessToQuizException.class)
 	public ResponseEntity<String> handleUnauthorizedAccessToQuizException(UnauthorizedAccessToQuizException e) {
 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
 	}
 	
 	@ExceptionHandler(UserNotFoundException.class)
 	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
 		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
 	}
 	
 	@ExceptionHandler(QuizNotFoundException.class)
 	public ResponseEntity<String> handleQuizNotFoundException(QuizNotFoundException e) {
 		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
 	}
}
