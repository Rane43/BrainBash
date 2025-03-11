package com.prometheus.brainbash.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.exception.QuizNotFoundException;
import com.prometheus.brainbash.exception.UserNotFoundException;
import com.prometheus.brainbash.mapper.QuizMapper;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IJwtService;
import com.prometheus.brainbash.service.IUserService;
import com.prometheus.brainbash.service.impl.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
	private QuizRepository quizRepo;
	private IUserService userService;
	private IJwtService jwtService;
	
	public QuizController(QuizRepository quizRepo, UserService userService, IJwtService jwtService) {
		this.quizRepo = quizRepo;
		this.userService = userService;
		this.jwtService = jwtService;
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
	
	
	@GetMapping
	public List<QuizSummaryDto> getAllQuizSummaries() {
		return quizRepo.findAll().stream().map((quiz) -> {
			QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
			QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
			return quizSummaryDto;
		}).toList();
	}
	
	@GetMapping("/mine")
	public List<QuizSummaryDto> getAllMyQuizSummaries(@RequestHeader("Authorization") String bearerToken) {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		
		return quizRepo.findByCreator_Username(username).stream().map((quiz) -> {
			QuizSummaryDto quizSummaryDto = new QuizSummaryDto();
			QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);
			return quizSummaryDto;
		}).toList();
	}
	
	
	@PostMapping
	public long createQuiz(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody QuizCreationDto quizCreationDto) throws UserNotFoundException {
		String username = jwtService.extractUsername(bearerToken.substring(7));
		User user = userService.getUser(username);
		
		Quiz quiz = new Quiz();
		QuizMapper.quizCreationDtoToQuiz(quizCreationDto, user, quiz);
		quizRepo.save(quiz);
		
		return quiz.getId();
	}
	
	@GetMapping("/{id}")
	public QuizGameDto getQuizById(@PathVariable long id) throws QuizNotFoundException {
		Optional<Quiz> quizOptional = quizRepo.findById(id);
		
		if (quizOptional.isEmpty()) {
			throw new QuizNotFoundException(id);
		}
		
		QuizGameDto quizGameDto = new QuizGameDto();
		QuizMapper.toQuizGameDto(quizOptional.get(), quizGameDto);
		
		return quizGameDto;
	}
	
	
	// --------------------- EXCEPTION HANDLER --------------------------
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(QuizNotFoundException.class)
	public ResponseEntity<String> handleQuizNotFoundException(QuizNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
