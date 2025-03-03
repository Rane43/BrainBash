package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Key;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/*
 * Unit Tests for JwtService
 */
class JwtServiceTest {
	private static final String TEST_SECRET_KEY = "6da136068d30d14fa0f4934e2a2afd94947240be44d6b71effc86dfd55750bf523755b231458eebb04db84dfaa6105c7f25de02b1f47448485a640abe6a0336c";
	private JwtService jwtService;
	
	@BeforeEach
	void setup() {
		jwtService = new JwtService();
		jwtService.setSecretKey(TEST_SECRET_KEY);
	}
	
	@Test
	void testGeneratedToken() {
		final String username = "testUsername";
		
		User user = new User();
		user.setUsername(username);
		
		Set<Role> roles = Set.of(Role.ROLE_ADMIN);
		user.setAuthorities(roles);
		
		String token = jwtService.generateToken(user);
		
		
        // Parse token and validate claims
        Claims claims = Jwts.parserBuilder()
        					.setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        
        // Subject is username
        assertEquals(username, claims.getSubject());
        
        // Roles are in jwt
        assertTrue(claims.containsKey("roles"));
        assertEquals("[ROLE_ADMIN]", claims.get("roles").toString());
	}
	
	// Helper functions
	private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(TEST_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
