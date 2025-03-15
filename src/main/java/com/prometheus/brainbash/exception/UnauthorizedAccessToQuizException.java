package com.prometheus.brainbash.exception;


public class UnauthorizedAccessToQuizException extends Exception {
	
	public UnauthorizedAccessToQuizException(long id) {
		super("You do not have access to quiz with id '" + id + "'");
	}
}
