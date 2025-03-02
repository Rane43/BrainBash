package com.prometheus.brainbash.login.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.login.dto.LoginDto;
import com.prometheus.brainbash.login.dto.LoginResponseDto;
import com.prometheus.brainbash.login.exception.InvalidCredentialsException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) throws InvalidCredentialsException {
		String token = "fakeTempToken";
		
		LoginResponseDto loginResponse = new LoginResponseDto(token);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
		
	}
}
