package com.prometheus.brainbash.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dto.QuestionCreationDto;
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
	@GetMapping // -- GET /api/quizzes
	public ResponseEntity<List<QuizSummaryDto>> getAllQuizSummaries() {
		return ResponseEntity.status(HttpStatus.OK).body(quizService.findAll());
	}
	
	@GetMapping("/{id}") // -- GET /api/quizzes/{id}
	public ResponseEntity<QuizGameDto> getQuizById(@PathVariable long id) throws QuizNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(quizService.findById(id));
	}
	
	@GetMapping("/mine") // {"Authorization": "Bearer {token}"} -- GET /api/quizzes/mine 
	@PreAuthorize("hasRole('QUIZ_DESIGNER')")
	public ResponseEntity<List<QuizSummaryDto>> getAllMyQuizSummaries(@RequestHeader("Authorization") String bearerToken) {
		return ResponseEntity.status(HttpStatus.OK).body(quizService.findMine(bearerToken));
	}
	
	@GetMapping("/search") // -- GET /api/quizzes/search 
	public ResponseEntity<List<QuizSummaryDto>> getQuizSummariesByTitleAndFilters(
	        @RequestParam String middleTitle, 
	        @RequestParam DifficultyRating difficultyRating,
	        @RequestParam AgeRating ageRating) {
		
	    return ResponseEntity.status(HttpStatus.OK).body(
	    		quizService.findBySearch(middleTitle, difficultyRating, ageRating));
	}
	
	
	@PostMapping // -- POST /api/quizzes {request body}
	@Transactional
	public ResponseEntity<Long> createQuiz(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody QuizCreationDto quizCreationDto) throws UserNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(quizService.save(bearerToken, quizCreationDto));
	}
	
	/*
	 * For reference
	 */
	
	/*
	@GetMapping("/courses")
    public List<EntityModel<CourseDTO>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<EntityModel<CourseDTO>> courseDTOs = new ArrayList<>();
        
        for (Course course : courses) {
            CourseDTO courseDTO = courseMapper.toDto(course);
            EntityModel<CourseDTO> entityModel = EntityModel.of(courseDTO);
            
            // Only add links if necessary
            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(JpaDemoController.class)
                .getCourseModules(course.getId())).withRel("modules").withType("GET"));
            
            courseDTOs.add(entityModel);
        }
        
        return courseDTOs;
    }
	*/
	
	
	/* ------------------- CRUD QUESTIONS -------------------------- */
	@GetMapping("/questions/{id}")  // -- GET /api/quizzes/questions/{id}
	public ResponseEntity<QuestionDto> getQuestionById(@PathVariable long id) throws QuestionNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(questionService.findById(id));
	}
	
	@PostMapping("/{quizId}/questions") // {"Authorization": "Bearer {token}"} -- POST /api/quizzes/{quizId}/questions {request body}
	@Transactional
	public ResponseEntity<Long> createQuestion(
			@RequestHeader("Authorization") String bearerToken, 
			@PathVariable long quizId,
			@Valid @RequestBody QuestionCreationDto questionCreationDto
	) throws UserNotFoundException, QuizNotFoundException, UnauthorizedAccessToQuizException {
		return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(bearerToken, quizId, questionCreationDto));
	}
}
