package com.prometheus.brainbash.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.prometheus.brainbash.dto.LoginDto;
import com.prometheus.brainbash.dto.LoginResponseDto;
import com.prometheus.brainbash.exception.InvalidCredentialsException;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IAuthService;
import com.prometheus.brainbash.service.IJwtService;

/*
 * Mock tests for AuthController
 */
class AuthControllerTest {
	private IAuthService authService;
	private IJwtService jwtService;
	
	private AuthController authController;
	
	@BeforeEach
	void setup() {
		authService = mock(IAuthService.class);
		jwtService = mock(IJwtService.class);
		
		authController = new AuthController(authService, jwtService);
	}
	
	@Test
	void testInvalidCredentials() throws InvalidCredentialsException {
		final String username = "InvalidEmail".toLowerCase();
		final String password = "InvalidPassword";
		
		final String invalid_credentials_message = "invalid credentials";
		when(authService.authenticate(username, password)).thenThrow(new InvalidCredentialsException(invalid_credentials_message));
		
		LoginDto loginDto = new LoginDto();
		loginDto.setPassword(password);
		loginDto.setUsername(username);
		
		Throwable e = assertThrows(InvalidCredentialsException.class, () -> {
			authController.login(loginDto);
		});
		assertEquals(invalid_credentials_message, e.getMessage());
		
		verify(authService, new Times(1)).authenticate(username, password);
		verify(jwtService, new Times(0)).generateToken(any());
	}
	
	@Test
	void testValidCredentials() throws InvalidCredentialsException {
		final String username = "validEmail".toLowerCase();
		final String password = "validPassword";
		final String token = "garble00000Token";
		
		UserDetails user = new User();
		when(authService.authenticate(username, password)).thenReturn(user);
		when(jwtService.generateToken(any())).thenReturn(token);
		
		LoginDto loginDto = new LoginDto();
		loginDto.setPassword(password);
		loginDto.setUsername(username);
		
		ResponseEntity<LoginResponseDto> loginResponse = authController.login(loginDto);
		assertEquals(token, loginResponse.getBody().getToken());
		
		verify(authService, new Times(1)).authenticate(username, password);
		verify(jwtService, new Times(1)).generateToken(any());
	}
	
}
