package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.exception.UserAlreadyExistsException;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;

class UserServiceTest {
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepo;
	
	private UserService userService;
	
	private User user;
	private String username = "testUsername";
	private String rawPassword = "testRawPassword";
	private Role role = Role.ROLE_QUIZ_DESIGNER;
	
	private final String userAlreadyExistsExceptionMessage = "User with username: " + username + " already exists.";
	
	@BeforeEach
	void setup() {
		passwordEncoder = mock(PasswordEncoder.class);
		userRepo = mock(UserRepository.class);
		
		userService = new UserService(userRepo, passwordEncoder);
		
		user = new User();
		user.setId(1L);
        user.setUsername(username);
        user.setPassword(rawPassword);
        user.setRole(role);
	}
	
	
	@Test
	void testCreateUserThrowsUserAlreadyExists() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		
		Throwable e = assertThrows(UserAlreadyExistsException.class, () -> {
			userService.createUser(username, rawPassword, role);
		});
		
		assertEquals(userAlreadyExistsExceptionMessage, e.getMessage());
		verify(passwordEncoder, new Times(0)).encode(rawPassword);
		verify(userRepo, new Times(0)).save(any(User.class));
	}
	
	@Test
	void testCreateUserSuccessfully() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
		
		userService.createUser(username, rawPassword, role);
		
		verify(passwordEncoder, new Times(1)).encode(rawPassword);
		verify(userRepo, new Times(1)).save(any(User.class));
	}

}
