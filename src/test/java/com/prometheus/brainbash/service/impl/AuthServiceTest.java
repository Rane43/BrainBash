package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.exception.InvalidCredentialsException;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;

/*
 * Mock tests for AuthService
 */
class AuthServiceTest {
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepo;
	
	private AuthService authService;
	
	
	@BeforeEach
	void setup() {
		passwordEncoder = new BCryptPasswordEncoder();
		userRepo = mock(UserRepository.class);
		
		authService = new AuthService(passwordEncoder, userRepo);
	}
	
	
	@Test
	void testInvalidUsername() {
		final String username = "invalidUsername".toLowerCase();
		final String password = "validPassword";
		
		when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
		
		Throwable e = assertThrows(InvalidCredentialsException.class, () -> {
			authService.authenticate(username, password);
		});
		assertEquals("Invalid username.", e.getMessage());
		
		verify(userRepo, new Times(1)).findByUsername(username);
	}
	
	@Test
	void testInvalidCredentials() {
		final String username = "validUsername".toLowerCase();
		final String validPassword = "validPassword";
		final String invalidPassword = "invalidPassword";
		
		User user = new User();
		user.setUsername(username);
		
		String encodedPassword = passwordEncoder.encode(validPassword);
		user.setPassword(encodedPassword);
		
		Set<Role> roles = Set.of(Role.ROLE_ADMIN);
		user.setAuthorities(roles);
		
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		
		Throwable e = assertThrows(InvalidCredentialsException.class, () -> {
			authService.authenticate(username, invalidPassword);
		});
		assertEquals("Invalid password.", e.getMessage());
		
		verify(userRepo, new Times(1)).findByUsername(username);
	}
	
	@Test
	void testValidPassword() throws InvalidCredentialsException {
		final String username = "validUsername".toLowerCase();
		final String password = "validPassword";
		
		User user = new User();
		user.setUsername(username);
		
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		
		Set<Role> roles = Set.of(Role.ROLE_ADMIN);
		user.setAuthorities(roles);
		
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		
		UserDetails userDetails = authService.authenticate(username, password);
		
		assertEquals(username, userDetails.getUsername());
		assertEquals(encodedPassword, userDetails.getPassword());
		assertEquals(roles, userDetails.getAuthorities());
		
		verify(userRepo, new Times(1)).findByUsername(username);
	}
	
}
