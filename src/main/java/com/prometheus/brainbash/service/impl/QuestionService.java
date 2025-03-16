package com.prometheus.brainbash.service.impl;

import org.springframework.stereotype.Service;

import com.prometheus.brainbash.dao.QuestionRepository;
import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.dto.QuestionRequestDto;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.exception.QuestionNotFoundException;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.mapper.QuestionMapper;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IJwtService;
import com.prometheus.brainbash.service.IQuestionService;


@Service
public class QuestionService implements IQuestionService {
	private IJwtService jwtService;
	private UserRepository userRepo;
	private QuizRepository quizRepo;
	private QuestionRepository questionRepo;
	
	public QuestionService(IJwtService jwtService, UserRepository userRepo, QuizRepository quizRepo,
			QuestionRepository questionRepo) {
		this.jwtService = jwtService;
		this.userRepo = userRepo;
		this.quizRepo = quizRepo;
		this.questionRepo = questionRepo;
	}


	/* ------------------- CRUD QUESTIONS -------------------------- */
	@Override
	public QuestionDto findById(long id) throws QuestionNotFoundException {
		Question question = questionRepo.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
		
		QuestionDto questionDto = new QuestionDto();
		QuestionMapper.toQuestionDto(question, questionDto);
		
		return questionDto;
	}
	
	@Override
	public long createQuestion(
			String bearerToken, 
			long quizId, 
			QuestionRequestDto questionCreationDto
	) throws UserNotFoundException, QuizNotFoundException, UnauthorizedAccessToQuizException {
		
		String username = jwtService.extractUsername(bearerToken.substring(7));
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		
		Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new QuizNotFoundException(quizId));
		
		if (!quiz.getDevelopers().contains(user)) throw new UnauthorizedAccessToQuizException(quizId);
		
		// Else Add Question to quiz
		Question question = new Question();
		QuestionMapper.toQuestion(questionCreationDto, quiz, question);
		
		question = questionRepo.save(question);
		
		quiz.addQuestion(question);
		quizRepo.save(quiz);
		
		return question.getId();
	}


	@Override
	public void updateQuestion(
			String bearerToken, 
			long questionId, 
			QuestionRequestDto questionDto
	) throws UserNotFoundException, QuestionNotFoundException, UnauthorizedAccessToQuizException {
		
		String username = jwtService.extractUsername(bearerToken.substring(7));
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		
		// Find question
		Question question = questionRepo.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(questionId));
		
		// Check has access
		Quiz quiz = question.getQuiz();
		if (!quiz.getDevelopers().contains(user)) throw new UnauthorizedAccessToQuizException(quiz.getId());

		// Update question details
		QuestionMapper.toQuestion(questionDto, quiz, question);
		
		// Save question with updated details
		questionRepo.save(question);
	}

}
