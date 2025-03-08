package com.prometheus.brainbash.model;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
	ROLE_QUIZZER, ROLE_QUIZ_DESIGNER, ROLE_ADMIN;

	@Override
	public String getAuthority() {
		return this.toString();
	}
}
