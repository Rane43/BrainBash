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
import com.prometheus.brainbash.exception.InvalidCredentialsException;
import com.prometheus.brainbash.service.IAuthService;
import com.prometheus.brainbash.service.IJwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private IAuthService authService;
	private IJwtService jwtService;
	
	public AuthController(IAuthService authService, IJwtService jwtService) {
		this.authService = authService;
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) throws InvalidCredentialsException {
		// Usernames are case insensitive
		String username = loginDto.getUsername().toLowerCase();
		
		UserDetails userDetails = authService.authenticate(username, loginDto.getPassword());
		String token = jwtService.generateToken(userDetails);
		
		LoginResponseDto loginResponse = new LoginResponseDto(token);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);	
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody LoginDto loginDto) throws InvalidCredentialsException {
		// Usernames are case insensitive
		String username = loginDto.getUsername().toLowerCase();
		
		UserDetails userDetails = authService.authenticate(username, loginDto.getPassword());
		String token = jwtService.generateToken(userDetails);
		
		LoginResponseDto loginResponse = new LoginResponseDto(token);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);	
	}
	
	
	
	// --------------- EXCEPTION HANDLERS ----------------
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleValidationException(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
