package com.prometheus.brainbash.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.mapper.QuizMapper;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IJwtService;
import com.prometheus.brainbash.service.IQuizService;


@Service
public class QuizService implements IQuizService {
	private QuizRepository quizRepo;
	private UserRepository userRepo;
	private IJwtService jwtService;
	
	public QuizService(QuizRepository quizRepo, UserRepository userRepo, IJwtService jwtService) {
		this.quizRepo = quizRepo;
		this.userRepo = userRepo;
		this.jwtService = jwtService;
	}

	@Override
	public List<QuizSummaryDto> findAll() {
		return quizRepo.findAll().stream().map((quiz) -> {
			QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
			QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
			return quizSummaryDto;
		}).toList();
	}

	@Override
	public List<QuizSummaryDto> findMine(String bearerToken) {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		
		return quizRepo.findByCreator_Username(username).stream().map((quiz) -> {
			QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
			QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
			return quizSummaryDto;
		}).toList();
	}
	
	@Override
	public List<QuizSummaryDto> findBySearch(
			String middleTitle, 
	        DifficultyRating difficultyRating,
	        AgeRating ageRating) {

	    return quizRepo.findBySearch(middleTitle, difficultyRating, ageRating)
	            .stream()
	            .map(quiz -> {
	                QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
	                QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
	                return quizSummaryDto;
	            })
	            .toList();
	}

	@Override
	public QuizGameDto findById(long id) throws QuizNotFoundException {
		Optional<Quiz> quizOptional = quizRepo.findById(id);
		
		if (quizOptional.isEmpty()) {
			throw new QuizNotFoundException(id);
		}
		
		QuizGameDto quizGameDto = new QuizGameDto();
		QuizMapper.toQuizGameDto(quizOptional.get(), quizGameDto);
		
		return quizGameDto;
	}

	@Override
	public long save(String bearerToken, QuizCreationDto quizCreationDto) throws UserNotFoundException {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		
		Quiz quiz = new Quiz();
		QuizMapper.quizCreationDtoToQuiz(quizCreationDto, user, quiz);
		quizRepo.save(quiz);
		
		return quiz.getId();
	}

	@Override
	public List<QuizSummaryDto> findMineBySearch(
			String bearerToken, 
			String middleTitle,
			DifficultyRating difficultyRating, 
			AgeRating ageRating) throws UnauthorizedAccessToQuizException, UserNotFoundException {
		
		String username = jwtService.extractUsername(bearerToken.substring(7));
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		
		return quizRepo.findMineBySearch(user, middleTitle, difficultyRating, ageRating)
		            .stream()
		            .map(quiz -> {
		                QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
		                QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
		                return quizSummaryDto;
		            })
		            .toList();
	}

	@Override
	public void delete(String bearerToken, long quizId)
			throws UnauthorizedAccessToQuizException, UserNotFoundException, QuizNotFoundException {
		
		String username = jwtService.extractUsername(bearerToken.substring(7));
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
		
		Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new QuizNotFoundException(quizId));
		
		if (!quiz.getDevelopers().contains(user)) throw new UnauthorizedAccessToQuizException(quizId);
		
		quizRepo.delete(quiz);
	}

}
