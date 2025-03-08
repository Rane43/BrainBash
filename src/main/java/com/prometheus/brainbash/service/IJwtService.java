package com.prometheus.brainbash.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
	
	String generateToken(UserDetails userDetails);
	
	boolean isTokenValid(UserDetails userDetails, String jwt);

	String extractUsername(String jwt);
}
