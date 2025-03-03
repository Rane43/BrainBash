package com.prometheus.brainbash.service.impl;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.prometheus.brainbash.service.IJwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;

@Service
@Setter
public class JwtService implements IJwtService {
	
	private String secretKey = "d0871dd97f055f508f239a990c55ba01b43937a71e8fb1fce2824adb63dd8577f8f1d7379b1a226bb412303e07b1cbac9555bd3c6deaa6333234762972c1b40d";
	
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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
