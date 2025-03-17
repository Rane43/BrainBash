package com.prometheus.brainbash.exception;

public class QuizNotFoundException extends RuntimeException {
	
	public QuizNotFoundException(long id) {
		super("Quiz with id: " + id + ", cannot be found.");
	}
}
