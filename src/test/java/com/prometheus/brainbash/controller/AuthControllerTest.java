package com.prometheus.brainbash.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.security.core.userdetails.UserDetails;

import com.prometheus.brainbash.dto.LoginDto;
import com.prometheus.brainbash.dto.LoginResponseDto;
import com.prometheus.brainbash.dto.RegDto;
import com.prometheus.brainbash.exception.InvalidCredentialsException;
import com.prometheus.brainbash.exception.UserAlreadyExistsException;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IAuthService;
import com.prometheus.brainbash.service.IJwtService;
import com.prometheus.brainbash.service.IUserService;

/*
 * Mock tests for AuthController
 */
class AuthControllerTest {
	private IUserService userService;
	private IAuthService authService;
	private IJwtService jwtService;
	
	private AuthController authController;
	
	@BeforeEach
	void setup() {
		userService = mock(IUserService.class);
		authService = mock(IAuthService.class);
		jwtService = mock(IJwtService.class);
		
		authController = new AuthController(userService, authService, jwtService);
	}
	
	// login
	@Test
	void testInvalidCredentials() throws InvalidCredentialsException {
		final String username = "InvalidEmail".toLowerCase();
		final String password = "InvalidPassword";
		
		final String invalid_credentials_message = "invalid credentials";
		
		final LoginDto loginDto = new LoginDto();
		loginDto.setPassword(password);
		loginDto.setUsername(username);
		
		when(authService.authenticate(username, password)).thenThrow(new InvalidCredentialsException(invalid_credentials_message));
		
		Throwable e = assertThrows(InvalidCredentialsException.class, () -> {
			authController.login(loginDto);
		});
		
		assertEquals(invalid_credentials_message, e.getMessage());
		verify(authService, new Times(1)).authenticate(username, password);
		verify(jwtService, new Times(0)).generateToken(any());
	}
	
	@Test
	void testValidCredentials() {
		final String username = "validEmail".toLowerCase();
		final String password = "validPassword";
		final String token = "garble00000Token";
		
		final UserDetails user = new User();
		
		final LoginDto loginDto = new LoginDto();
		loginDto.setPassword(password);
		loginDto.setUsername(username);
		
		when(authService.authenticate(username, password)).thenReturn(user);
		when(jwtService.generateToken(any())).thenReturn(token);
		
		LoginResponseDto loginResponse = assertDoesNotThrow(() -> authController.login(loginDto));
		
		assertEquals(token, loginResponse.getToken());
		verify(authService, new Times(1)).authenticate(username, password);
		verify(jwtService, new Times(1)).generateToken(any());
	}
	
	// register
	@Test
	void shouldThrowUserAlreadyExistsExceptionWhenReregistering() {
		final String username = "InvalidEmail".toLowerCase();
		final String password = "InvalidPassword";
		final Role role = Role.ROLE_QUIZ_DESIGNER;
		
		final String invalid_credentials_message = "User with username: " + username + " already exists.";
		when(userService.createUser(username, password, role)).thenThrow(new UserAlreadyExistsException(username));
		
		RegDto regDto = new RegDto();
		regDto.setPassword(password);
		regDto.setUsername(username);
		regDto.setRole(role);
		
		Throwable e = assertThrows(UserAlreadyExistsException.class, () -> {
			authController.register(regDto);
		});
		
		assertEquals(invalid_credentials_message, e.getMessage());
		verify(userService, new Times(1)).createUser(username, password, role);
		verify(jwtService, new Times(0)).generateToken(any());
	}
	
	@Test
	void shouldSuccessfullyRegisterUser() {
		final String username = "InvalidEmail".toLowerCase();
		final String password = "InvalidPassword";
		final Role role = Role.ROLE_QUIZ_DESIGNER;
		final User user = new User();
		
		final RegDto regDto = new RegDto();
		regDto.setPassword(password);
		regDto.setUsername(username);
		regDto.setRole(role);
		
		final String token = "validToken";
		
		when(userService.createUser(username, password, role)).thenReturn(user);
		when(jwtService.generateToken(any())).thenReturn(token);
		
		LoginResponseDto loginResponse = assertDoesNotThrow(() -> authController.register(regDto));
		
		assertEquals(token, loginResponse.getToken());
		verify(userService, new Times(1)).createUser(username, password, role);
		verify(jwtService, new Times(1)).generateToken(any());
	}
}
