package com.prometheus.brainbash.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.exception.UserAlreadyExistsException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IUserService;

@Service
public class UserService implements IUserService {
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public User createUser(String username, String password, Role role) throws UserAlreadyExistsException {
		username = username.toLowerCase();
		
	    // Check if the user already exists
	    if (userRepo.findByUsername(username).isPresent()) {
	        throw new UserAlreadyExistsException(username);
	    }
	    
	    // Create new user and set hashed
	    User user = new User();
	    user.setUsername(username);
	    user.setPassword(passwordEncoder.encode(password));
	    user.setRole(role);
	    
	    // Save the user to the repository
	    return userRepo.save(user);
	}

	@Override
	public User getUser(String username) throws UserNotFoundException {
		return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
	}
	
}
