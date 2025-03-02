package com.prometheus.brainbash.service.impl;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.prometheus.brainbash.service.IJwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtService implements IJwtService {
	
	private static final String SECRET_KEY = "";
	
	@Override
	public String generateToken(UserDetails userDetails) {
	    // Extract all user roles
	    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
	    List<String> roles = authorities.stream()
	            .map(GrantedAuthority::getAuthority)
	            .toList();

	    Map<String, Object> claims = new HashMap<>();
	    claims.put("roles", roles); // Store all roles in JWT
	    
	    return Jwts.builder()
	            .setClaims(claims)
	            .setSubject(userDetails.getUsername())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiry
	            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
	            .compact();
	}
	
	// Helper functions
	private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
