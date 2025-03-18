package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/*
 * Unit Tests for JwtService
 */
class JwtServiceTest {
	private static final String TEST_SECRET_KEY = "6da136068d30d14fa0f4934e2a2afd94947240be44d6b71effc86dfd55750bf523755b231458eebb04db84dfaa6105c7f25de02b1f47448485a640abe6a0336c";
	private JwtService jwtService;
	
	private static final String USERNAME = "USER-NAME_123";
	private static final String PASSWORD = "TestPassword123!";
	private static final Set<Role> ROLES = Set.of(Role.ROLE_QUIZZER);
	
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
		user.setRole(Role.ROLE_QUIZZER);
		
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
        assertEquals("[ROLE_QUIZZER]", claims.get("roles").toString());
	}
	
	@Test
    void testNonValidJwt() {
        String invalidToken = "invalid.jwt.token";
        boolean isValid = jwtService.isTokenValid(setupUser(), invalidToken);
        assertFalse(isValid, "Invalid JWT should return false");
    }

    @Test
    void testValidJwt() {
    	UserDetails user = setupUser();
    	String jwt = createValidJwt(user);
    	
        boolean isValid = jwtService.isTokenValid(user, jwt);
        assertTrue(isValid, "Valid JWT should return true");
    }

    @Test
    void testExtractUsernameInvalidJwt() {
        String invalidToken = "invalid.jwt.token";

        Throwable e = assertThrows(IllegalArgumentException.class, () -> {
            jwtService.extractUsername(invalidToken);
        });
        assertTrue(e.getMessage().contains("Issue with jwt"), "Exception should be thrown for invalid JWT");
    }

    @Test
    void testExtractUsernameValidJwt() {
    	String validToken = createValidJwt(setupUser());
        String username = jwtService.extractUsername(validToken);
        assertEquals(USERNAME, username);
    }

    
	// Helper functions
    private String createValidJwt(UserDetails user) {
    	Map<String, Object> claims = new HashMap<>();
 	    claims.put("roles", user.getAuthorities().stream()
 	            .map(GrantedAuthority::getAuthority)
 	            .toList()); 
     	
 	    return Jwts.builder()
 		        .setClaims(claims)
 		        .setSubject(user.getUsername())
 		        .setIssuedAt(new Date(System.currentTimeMillis()))
 		        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiry
 		        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
 		        .compact();
    }
    
    private UserDetails setupUser() {
    	User user = new User();
		user.setUsername(USERNAME);
		user.setPassword(new BCryptPasswordEncoder().encode(PASSWORD));
		user.setRole(Role.ROLE_QUIZZER);
		
    	return user;
    }
	private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(TEST_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
