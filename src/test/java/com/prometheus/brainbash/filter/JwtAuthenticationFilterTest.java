package com.prometheus.brainbash.filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IJwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtAuthenticationFilterTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    private IJwtService jwtService;
    private UserRepository userRepo;

    private JwtAuthenticationFilter jwtAuthFilter;

    @BeforeEach
    void setup() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        jwtService = mock(IJwtService.class);
        userRepo = mock(UserRepository.class);

        jwtAuthFilter = new JwtAuthenticationFilter(jwtService, userRepo);
        
        // Clear Security Context before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterChainIfNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueFilterChainIfAuthorizationHeaderIsInvalid() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidToken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSetUnauthorizedIfTokenIsInvalid() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenThrow(new IllegalArgumentException());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotContinueFilterChainIfUserNotFound() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user123");
        when(userRepo.findByUsername("user123")).thenReturn(Optional.empty());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        verify(response, new Times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    void shouldNotContinueFilterChainIfTokenIsntValid() throws ServletException, IOException {
    	User user = mock(User.class);
    	
    	when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user123");
        when(userRepo.findByUsername("user123")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(any(User.class), any(String.class))).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);
        verify(response, new Times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateUserIfTokenIsValid() throws ServletException, IOException {
        User user = mock(User.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("user123");
        when(userRepo.findByUsername("user123")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(user, "validToken")).thenReturn(true);
        when(user.getAuthorities()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
