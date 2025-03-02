package com.prometheus.brainbash.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.prometheus.brainbash.exception.InvalidCredentialsException;

public interface IAuthService {
	UserDetails authenticate(String username, String password) throws InvalidCredentialsException;
}
