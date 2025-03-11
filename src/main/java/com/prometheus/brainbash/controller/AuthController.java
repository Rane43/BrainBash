package com.prometheus.brainbash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dto.LoginDto;
import com.prometheus.brainbash.dto.LoginResponseDto;
import com.prometheus.brainbash.dto.RegDto;
import com.prometheus.brainbash.exception.InvalidCredentialsException;
import com.prometheus.brainbash.exception.UserAlreadyExistsException;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IAuthService;
import com.prometheus.brainbash.service.IJwtService;
import com.prometheus.brainbash.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private IUserService userService;
	private IAuthService authService;
	private IJwtService jwtService;
	
	public AuthController(IUserService userService, IAuthService authService, IJwtService jwtService) {
		this.userService = userService;
		this.authService = authService;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) throws InvalidCredentialsException {
		// Usernames are case insensitive
		String username = loginDto.getUsername().toLowerCase().trim();
		String password = loginDto.getPassword().trim();
		
		UserDetails userDetails = authService.authenticate(username,password);
		String token = jwtService.generateToken(userDetails);
		
		LoginResponseDto loginResponse = new LoginResponseDto(token);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);	
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody RegDto regDto) throws UserAlreadyExistsException {
		// Usernames are case insensitive
		String username = regDto.getUsername().toLowerCase().trim();
		String password = regDto.getPassword().trim();
		
		User user = userService.createUser(username, password, regDto.getRole());
		String token = jwtService.generateToken(user);
		
		LoginResponseDto loginResponse = new LoginResponseDto(token);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);	
	}
	
	
	
	// --------------- EXCEPTION HANDLERS ----------------
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
