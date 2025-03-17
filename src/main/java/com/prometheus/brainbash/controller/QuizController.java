package com.prometheus.brainbash.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dto.QuestionRequestDto;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.exception.QuestionNotFoundException;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UnauthorizedAccessToQuizException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.service.IQuestionService;
import com.prometheus.brainbash.service.IQuizService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
	private IQuizService quizService;
	private IQuestionService questionService;
	
	public QuizController(IQuizService quizService, IQuestionService questionService) {
		this.quizService = quizService;
		this.questionService = questionService;
	}
	
	
	/* ------------------- CRUD QUIZZES -------------------------- */
	@GetMapping("/{id}") // -- GET /api/quizzes/{id}
	public ResponseEntity<QuizGameDto> getQuizById(@PathVariable long id) throws QuizNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(quizService.findById(id));
	}
	
	@GetMapping("/mine/search")
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	public CollectionModel<EntityModel<QuizSummaryDto>> getMyQuizSummariesByTitleAndFilters(
		@RequestHeader("Authorization") String bearerToken,
		@RequestParam(required=false) String middleTitle, 
		@RequestParam(required=false) DifficultyRating difficultyRating,
		@RequestParam(required=false) AgeRating ageRating
	) throws UserNotFoundException, UnauthorizedAccessToQuizException {
		List<EntityModel<QuizSummaryDto>> quizzes = quizService.findMineBySearch(bearerToken, middleTitle, difficultyRating, ageRating)
			.stream()
			.map((QuizSummaryDto quiz) -> EntityModel.of(quiz,
				linkTo(methodOn(QuizController.class).getQuizById(quiz.getId())).withSelfRel(),
				linkTo(methodOn(QuizController.class).deleteQuiz(bearerToken, quiz.getId())).withRel("delete")
			))
			.toList();

		// Add links to create quiz, get categories, and get age ratings
		Link createQuizLink = linkTo(methodOn(QuizController.class).createQuiz(bearerToken, null)).withRel("createQuiz");
		Link getCategoriesLink = linkTo(methodOn(CategoryController.class).getAllCategories()).withRel("categories");
		Link getAgeRatingsLink = linkTo(methodOn(AgeRatingController.class).getAllAgeRatings()).withRel("ageRatings");
		Link getDifficultyRatingsLink = linkTo(methodOn(DifficultyRatingController.class).getAllDifficultyRatings()).withRel("difficultyRatings");
		Link getImagesLink = linkTo(methodOn(ImageController.class).getAllQuizImages()).withRel("quizImages");

		return CollectionModel.of(quizzes, createQuizLink, getCategoriesLink, getAgeRatingsLink, getDifficultyRatingsLink, getImagesLink);
	}
	
	/*
	@GetMapping("/mine/search") // {"Authorization": "Bearer {token}"} -- GET /api/quizzes/mine/search?middleTitle={}&difficultyRating={}&ageRating={} 
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	public ResponseEntity<List<QuizSummaryDto>> getMyQuizSummariesByTitleAndFilters(
			@RequestHeader("Authorization") String bearerToken,
	        @RequestParam(required=false) String middleTitle, 
	        @RequestParam(required=false) DifficultyRating difficultyRating,
	        @RequestParam(required=false) AgeRating ageRating) throws UserNotFoundException, UnauthorizedAccessToQuizException {
	    return ResponseEntity.status(HttpStatus.OK).body(
	    		quizService.findMineBySearch(bearerToken, middleTitle, difficultyRating, ageRating));
	}
	*/
	
	@GetMapping("/search") // -- GET /api/quizzes/search?middleTitle={}&difficultyRating={}&ageRating={} 
	@PreAuthorize("hasRole('QUIZZER')")
	public ResponseEntity<List<QuizSummaryDto>> getQuizSummariesByTitleAndFilters(
	        @RequestParam(required=false) String middleTitle, 
	        @RequestParam(required=false) DifficultyRating difficultyRating,
	        @RequestParam(required=false) AgeRating ageRating) {

	    return ResponseEntity.status(HttpStatus.OK).body(
	    		quizService.findBySearch(middleTitle, difficultyRating, ageRating));
	}
	
	
	@PostMapping // -- POST /api/quizzes {request body}
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	@Transactional
	public ResponseEntity<Long> createQuiz(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody QuizCreationDto quizCreationDto) throws UserNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(quizService.save(bearerToken, quizCreationDto));
	}
	
	@DeleteMapping("/{quizId}")
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	@Transactional
	public ResponseEntity<Void> deleteQuiz(
			@RequestHeader("Authorization") String bearerToken,
			@PathVariable long quizId
	) throws UnauthorizedAccessToQuizException, UserNotFoundException, QuizNotFoundException {
		quizService.delete(bearerToken, quizId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	
	/* ------------------- CRUD QUESTIONS -------------------------- */
	@GetMapping("/questions/{id}")  // -- GET /api/quizzes/questions/{id}
	@PreAuthorize("hasRole('QUIZZER') or hasRole('QUIZ_DESIGNER')")
	public ResponseEntity<QuestionDto> getQuestionById(@PathVariable long id) throws QuestionNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(questionService.findById(id));
	}
	
	@PostMapping("/{quizId}/questions") // {"Authorization": "Bearer {token}"} -- POST /api/quizzes/{quizId}/questions {request body}
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	@Transactional
	public ResponseEntity<Long> createQuestion(
			@RequestHeader("Authorization") String bearerToken, 
			@PathVariable long quizId,
			@Valid @RequestBody QuestionRequestDto questionCreationDto
	) throws UserNotFoundException, QuizNotFoundException, UnauthorizedAccessToQuizException {
		return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(bearerToken, quizId, questionCreationDto));
	}
	
	
	@PutMapping("/questions/{questionId}")
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	@Transactional
	public ResponseEntity<Void> updateQuestion(
			@RequestHeader("Authorization") String bearerToken, 
			@PathVariable long questionId,
			@Valid @RequestBody QuestionRequestDto questionRequestDto
	) throws UserNotFoundException, QuestionNotFoundException, UnauthorizedAccessToQuizException 
	{
		questionService.updateQuestion(bearerToken, questionId, questionRequestDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/questions/{questionId}")
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	@Transactional
	public ResponseEntity<Void> deleteQuestion(
			@RequestHeader("Authorization") String bearerToken, 
			@PathVariable long questionId
	) throws UserNotFoundException, QuestionNotFoundException, UnauthorizedAccessToQuizException {
		questionService.delete(bearerToken, questionId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
