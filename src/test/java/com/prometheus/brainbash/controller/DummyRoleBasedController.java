package com.prometheus.brainbash.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test/api/roles")
public class DummyRoleBasedController {
	private static final String SUCCESS_MESSAGE = "All Good!";
	
	// QUIZZER ENDPOINT
	@GetMapping("quizzer")
	@PreAuthorize("hasRole('QUIZZER')")
	public String quizzer() {
		return SUCCESS_MESSAGE;
	}
	
	// QUIZ_DESIGNER ENDPOINT
	@GetMapping("quiz_designer")
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	public String quizDesigner() {
		return SUCCESS_MESSAGE;
	}
	
	// ADMIN ENDPOINT
	@GetMapping("admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin() {
		return SUCCESS_MESSAGE;
	}
}
