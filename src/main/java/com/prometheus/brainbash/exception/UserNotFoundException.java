package com.prometheus.brainbash.exception;

public class UserNotFoundException extends Exception {
	
	public UserNotFoundException(String username) {
		super("User with username: " + username + ", cannot be found.");
	}
	
}
