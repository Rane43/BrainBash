package com.prometheus.brainbash.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.prometheus.brainbash.dao.QuestionRepository;
import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.dto.QuestionRequestDto;
import com.prometheus.brainbash.exception.*;
import com.prometheus.brainbash.model.*;
import com.prometheus.brainbash.service.IJwtService;

class QuestionServiceTest {
    private IJwtService jwtService;
    private UserRepository userRepo;
    private QuizRepository quizRepo;
    private QuestionRepository questionRepo;
    private QuestionService questionService;

    private User user;
    private Quiz quiz;
    private Question question;
    private String username = "testUser";
    private String bearerToken = "Bearer mockToken";
    private long quizId = 1L;
    private long questionId = 2L;

    @BeforeEach
    public void setup() {
        jwtService = mock(IJwtService.class);
        userRepo = mock(UserRepository.class);
        quizRepo = mock(QuizRepository.class);
        questionRepo = mock(QuestionRepository.class);

        questionService = new QuestionService(jwtService, userRepo, quizRepo, questionRepo);

        user = new User();
        user.setId(1L);
        user.setUsername(username);

        quiz = new Quiz();
        quiz.setId(quizId);
        quiz.getDevelopers().add(user);

        question = new Question();
        question.setId(questionId);
        question.setQuiz(quiz);
    }

    @Test
    void testFindByIdSuccessfully() {
        when(questionRepo.findById(questionId)).thenReturn(Optional.of(question));
        QuestionDto result = questionService.findById(questionId);
        assertNotNull(result);
    }

    @Test
    void testFindByIdThrowsQuestionNotFoundException() {
        when(questionRepo.findById(questionId)).thenReturn(Optional.empty());
        Throwable e = assertThrows(QuestionNotFoundException.class, () -> {
        	questionService.findById(questionId);
        });
        assertEquals("Question with id: '" + questionId + "' cannot be found.", e.getMessage());
        
    }

    @Test
    void testCreateQuestionSuccessfully() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(quizRepo.findById(quizId)).thenReturn(Optional.of(quiz));
        when(questionRepo.save(any())).thenReturn(question);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        questionRequestDto.setAnswerRequestDtos(new HashSet<>());
        long createdQuestionId = questionService.createQuestion(bearerToken, quizId, questionRequestDto);
        
        assertEquals(questionId, createdQuestionId);
        verify(questionRepo, times(1)).save(any());
    }

    @Test
    void testCreateQuestionThrowsUserNotFoundException() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        assertThrows(UserNotFoundException.class, () -> questionService.createQuestion(bearerToken, quizId, questionRequestDto));
    }

    @Test
    void testCreateQuestionThrowsQuizNotFoundException() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(quizRepo.findById(quizId)).thenReturn(Optional.empty());

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        assertThrows(QuizNotFoundException.class, () -> questionService.createQuestion(bearerToken, quizId, questionRequestDto));
    }

    @Test
    void testUpdateQuestionSuccessfully() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(questionRepo.findById(questionId)).thenReturn(Optional.of(question));

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        questionRequestDto.setAnswerRequestDtos(new HashSet<>());
        assertDoesNotThrow(() -> questionService.updateQuestion(bearerToken, questionId, questionRequestDto));
        verify(questionRepo, times(1)).save(any());
    }

    @Test
    void testUpdateQuestionThrowsUnauthorizedAccessException() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        when(jwtService.extractUsername(any())).thenReturn("anotherUser");
        when(userRepo.findByUsername("anotherUser")).thenReturn(Optional.of(anotherUser));
        when(questionRepo.findById(questionId)).thenReturn(Optional.of(question));

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        assertThrows(UnauthorizedAccessToQuizException.class, () -> questionService.updateQuestion(bearerToken, questionId, questionRequestDto));
    }

    @Test
    void testDeleteQuestionSuccessfully() {
        when(jwtService.extractUsername(any())).thenReturn(username);
        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(questionRepo.findById(questionId)).thenReturn(Optional.of(question));

        assertDoesNotThrow(() -> questionService.delete(bearerToken, questionId));
        verify(questionRepo, times(1)).delete(question);
    }

    @Test
    void testDeleteQuestionThrowsUnauthorizedAccessException() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        when(jwtService.extractUsername(any())).thenReturn("anotherUser");
        when(userRepo.findByUsername("anotherUser")).thenReturn(Optional.of(anotherUser));
        when(questionRepo.findById(questionId)).thenReturn(Optional.of(question));

        assertThrows(UnauthorizedAccessToQuizException.class, () -> questionService.delete(bearerToken, questionId));
    }
}

