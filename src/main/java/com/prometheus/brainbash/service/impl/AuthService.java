package com.prometheus.brainbash.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.exception.InvalidCredentialsException;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IAuthService;

@Service
public class AuthService implements IAuthService {
	private PasswordEncoder passwordEncoder;
	private UserRepository userRepo;
	
	public AuthService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails authenticate(String username, String password) throws InvalidCredentialsException {
		Optional<User> userOptional = userRepo.findByUsername(username);
		
		if (userOptional.isEmpty()) {
			throw new InvalidCredentialsException("Invalid username.");
		}
		
		User user = userOptional.get();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new InvalidCredentialsException("Invalid password.");
		}
		
		return user;
	}
	
	
}
