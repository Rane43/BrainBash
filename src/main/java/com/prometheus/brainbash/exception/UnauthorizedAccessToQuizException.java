package com.prometheus.brainbash.exception;


public class UnauthorizedAccessToQuizException extends RuntimeException {
	
	public UnauthorizedAccessToQuizException(long id) {
		super("You do not have access to quiz with id '" + id + "'");
	}
}
