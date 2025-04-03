package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IJwtService;

class QuizServiceTest {
    private QuizRepository quizRepo;
    private UserRepository userRepo;
    private IJwtService jwtService;
    private QuizService quizService;

    private User user;
    private Quiz quiz;
    private String username = "testUser";
    private String bearerToken = "Bearer mockToken";
    private long quizId = 1L;
    
    private final String userNotFoundExceptionMessage = "User with username: " + username + ", cannot be found.";
    private final String quizNotFoundExceptionMessage = "Quiz with id: " + quizId + ", cannot be found.";
    private final String unauthorizedAccessToQuizException = "You do not have access to quiz with id '" + quizId + "'";
    
    @BeforeEach
    void setup() {
        quizRepo = mock(QuizRepository.class);
        userRepo = mock(UserRepository.class);
        jwtService = mock(IJwtService.class);
        
        quizService = new QuizService(quizRepo, userRepo, jwtService);
        
        user = new User();
        user.setId(1L);
        user.setUsername(username);
        
        quiz = new Quiz();
        quiz.setId(quizId);
    }
    
    // findAll
    @Test
    void testFindAllSuccessfully() {
        when(quizRepo.findAll()).thenReturn(List.of(quiz));
        List<QuizSummaryDto> result = quizService.findAll();
        assertFalse(result.isEmpty());
    }
    
    // findBySearch
    @Test
    void testFindBySearchSuccessfully() {
        when(quizRepo.findBySearch(any(), any(), any())).thenReturn(List.of(quiz));
        List<QuizSummaryDto> result = quizService.findBySearch("title", DifficultyRating.EASY, AgeRating.ADULT);
        assertFalse(result.isEmpty());
    }

    // findById
    @Test
    void testFindByIdThrowsQuizNotFoundException() {
        when(quizRepo.findById(quizId)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(QuizNotFoundException.class, () -> {
        	quizService.findById(quizId);
        });
        
        assertEquals(quizNotFoundExceptionMessage, e.getMessage());
    }
    
    @Test
    void testFindByIdSuccessfully() {
        when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
        QuizGameDto result = quizService.findById(quizId);
        assertNotNull(result);
    }

    // save
    @Test
    void testSaveThrowsUserNotFoundException() {
    	QuizCreationDto quizCreationDto = new QuizCreationDto();
    	
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
        	quizService.save(bearerToken, quizCreationDto);
        });
    
        assertEquals(userNotFoundExceptionMessage, e.getMessage());
        verify(quizRepo, new Times(0)).save(any());
    }
    
    @Test
    void testSaveSuccessfully() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(quizRepo.save(any())).thenReturn(quiz);
        
        QuizCreationDto quizCreationDto = new QuizCreationDto();
        long createdQuizId = quizService.save(bearerToken, quizCreationDto);
        
        assertEquals(quizId, createdQuizId);
        verify(quizRepo, times(1)).save(any(Quiz.class));
    }
    
    // findMineBySearch
    @Test
    void testFindMineBySearchThrowsUserNotFoundException() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
        	quizService.findMineBySearch(bearerToken, "title", DifficultyRating.EASY, AgeRating.ADULT);
        });
        
        assertEquals(userNotFoundExceptionMessage, e.getMessage());
    }
    
    @Test
    void testFindMineBySearch() throws UserNotFoundException {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(quizRepo.findMineBySearch(any(), any(), any(), any())).thenReturn(List.of(quiz));
        
        List<QuizSummaryDto> result = quizService.findMineBySearch(bearerToken, "title", DifficultyRating.EASY, AgeRating.ADULT);
        assertFalse(result.isEmpty());
    }

    
    // delete
    @Test
    void testDeleteThrowsUserNotFoundException() {
    	when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(UserNotFoundException.class, () -> {
        	quizService.delete(bearerToken, quizId);
        });
        
        assertEquals(userNotFoundExceptionMessage, e.getMessage());
        verify(quizRepo, new Times(0)).delete(any());
    }
    
    @Test
    void testDeleteThrowsQuizNotFoundException() {
    	when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(quizRepo.findById(quizId)).thenReturn(Optional.empty());
        
        Throwable e = assertThrows(QuizNotFoundException.class, () -> {
        	quizService.delete(bearerToken, quizId);
        });
        
        assertEquals(quizNotFoundExceptionMessage, e.getMessage());
        verify(quizRepo, new Times(0)).delete(any());
    }
    
    @Test
    void testDeleteThrowsUnauthorizedAccessException() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUsername(username);
        
        when(jwtService.extractUsername(any())).thenReturn("anotherUser");
        when(userRepo.findByUsername("anotherUser")).thenReturn(Optional.of(anotherUser));
        when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
        
        Throwable e = assertThrows(UnauthorizedAccessToQuizException.class, () -> {
        	quizService.delete(bearerToken, quizId);
        });
        
        assertEquals(unauthorizedAccessToQuizException, e.getMessage());
        verify(quizRepo, new Times(0)).delete(any());
    }
    
    @Test
    void testDeleteSuccessfully() {
    	quiz.getDevelopers().add(user);
    	
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
        
        assertDoesNotThrow(() -> quizService.delete(bearerToken, quizId));
        verify(quizRepo, new Times(1)).delete(quiz);
    }
}