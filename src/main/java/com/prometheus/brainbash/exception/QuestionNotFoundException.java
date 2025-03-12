package com.prometheus.brainbash.exception;

public class QuestionNotFoundException extends Exception {
	
	public QuestionNotFoundException(long id) {
		super("Question with id: '" + id + "' cannot be found.");
	}
}
