 package com.prometheus.brainbash;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.Answer;
import com.prometheus.brainbash.model.Category;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.service.IUserService;

@SpringBootApplication
public class BrainBashApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrainBashApplication.class, args);
	}
	
	// Temp Quiz created - hard coded for development
	@Bean
    CommandLineRunner init(IUserService userService, PasswordEncoder passwordEncoder, QuizRepository quizRepo) {
        return args -> {
        	// User to create quiz
        	User user = userService.createUser("Rane43", "Password123!", Role.ROLE_QUIZ_DESIGNER);
        	User developer = userService.createUser("NewUser", "Password123!", Role.ROLE_QUIZ_DESIGNER);
        	
        	final String DEFAULT_IMAGE = "landscape1.webp";
        	final String OTHER_IMAGE = "background-image.webp";
        	
        	
        	// AgeRating = TEEN, DfficultyRating = EASY
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 1", "Easy Teen Geography Quiz 1", AgeRating.TEEN, DifficultyRating.EASY, DEFAULT_IMAGE, quizRepo, user, developer);
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 2", "Easy Teen Geography Quiz 2", AgeRating.TEEN, DifficultyRating.EASY, OTHER_IMAGE, quizRepo, user, developer);
        	
        	// AgeRating = CHILDREN, DfficultyRating = EASY
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 3", "Easy Children's Geography Quiz 3", AgeRating.CHILDREN, DifficultyRating.EASY, DEFAULT_IMAGE, quizRepo, user, developer);
        	
        	// AgeRating = ADULT, DifficultyRating = EASY
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 4", "Easy adult quiz", AgeRating.ADULT, DifficultyRating.EASY, OTHER_IMAGE, quizRepo, user, developer);
        	
        	// AgeRating = ADULT, DifficultyRating = EASY
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 5", "Medium difficulty adult quiz", AgeRating.ADULT, DifficultyRating.MEDIUM, DEFAULT_IMAGE, quizRepo, user, developer);
        	
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 6", "Hard adult quiz", AgeRating.ADULT, DifficultyRating.HARD, OTHER_IMAGE, quizRepo, user, developer);
        	
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 7", "Extremely difficult adult quiz", AgeRating.ADULT, DifficultyRating.EXTREME, DEFAULT_IMAGE, quizRepo, user, developer);
        	
        	
        	createTempQuiz(Category.GEOGRAPHY, "Geography Quiz 8", "Geography Description 8", AgeRating.ADULT, DifficultyRating.MEDIUM, OTHER_IMAGE, quizRepo, user, developer); 
        	
        	
        	// ADULT MEDIUM
        	createTempQuiz(Category.ANATOMY, "Anatomy Quiz 1", "Anatomy Description 1", AgeRating.ADULT, DifficultyRating.MEDIUM, OTHER_IMAGE, quizRepo, user, developer);  
        	createTempQuiz(Category.HISTORY, "History Quiz 1", "History Description 1", AgeRating.ADULT, DifficultyRating.MEDIUM, DEFAULT_IMAGE, quizRepo, user, developer);  
        	createTempQuiz(Category.HISTORY, "History Quiz 2", "History Description 2", AgeRating.ADULT, DifficultyRating.MEDIUM, OTHER_IMAGE, quizRepo, user, developer);  
        	createTempQuiz(Category.HISTORY, "History Quiz 3", "History Description 3", AgeRating.ADULT, DifficultyRating.MEDIUM, DEFAULT_IMAGE, quizRepo, user, developer);  
        	createTempQuiz(Category.HISTORY, "History Quiz 4", "History Description 4", AgeRating.ADULT, DifficultyRating.MEDIUM, OTHER_IMAGE, quizRepo, user, developer);  
        	createTempQuiz(Category.HISTORY, "History Quiz 5", "History Description 5", AgeRating.ADULT, DifficultyRating.MEDIUM, DEFAULT_IMAGE, quizRepo, user, developer);  
        };
    }
	
	private void createTempQuiz(Category category, String title, String description, AgeRating ageRating, DifficultyRating difficultyRating, String image, QuizRepository quizRepo, User user, User developer) {
    	// Quiz itself
    	Quiz quiz = new Quiz();
    	quiz.setTitle(title);
    	quiz.setDescription(description);
    	quiz.setAgeRating(ageRating);
    	quiz.setCategory(category);
    	quiz.setDifficultyRating(difficultyRating);
    	// Add user as creator and as one of the developers
    	quiz.setCreator(user);
    	quiz.setDevelopers(Set.of(user, developer));
    	quiz.setImage(image);
    	
    	// ------------------ Question 1 --------------------
    	// First question for the quiz
    	Question question1 = new Question();
    	question1.setQuiz(quiz);
    	question1.setText("What is the capital of Germany?");
    	
    	
    	// Answers for the first question
    	Answer answer1 = new Answer();
    	answer1.setQuestion(question1);
    	answer1.setCorrect(true);
    	answer1.setText("Berlin");
    	
    	Answer answer2 = new Answer();
    	answer2.setQuestion(question1);
    	answer2.setCorrect(false);
    	answer2.setText("France");
    	
    	Answer answer3 = new Answer();
    	answer3.setQuestion(question1);
    	answer3.setCorrect(false);
    	answer3.setText("Chicago");
    	
    	// Save answers to question object
    	Set<Answer> answers = new HashSet<>();
    	answers.add(answer1);
    	answers.add(answer2);
    	answers.add(answer3);
    	
    	question1.setAnswers(answers);
    	
    	
    	// ------------------ Question 2 --------------------
    	// First question for the quiz
    	Question question2 = new Question();
    	question2.setQuiz(quiz);
    	question2.setText("How many continents are there?");
    	
    	
    	// Answers for the first question
    	Answer answer21 = new Answer();
    	answer21.setQuestion(question2);
    	answer21.setCorrect(true);
    	answer21.setText("7");
    	
    	Answer answer22 = new Answer();
    	answer22.setQuestion(question2);
    	answer22.setCorrect(false);
    	answer22.setText("1");
    	
    	Answer answer23 = new Answer();
    	answer23.setQuestion(question2);
    	answer23.setCorrect(false);
    	answer23.setText("2");
    	
    	// Save answers to question object
    	Set<Answer> answers2 = new HashSet<>();
    	answers2.add(answer21);
    	answers2.add(answer22);
    	answers2.add(answer23);
    	
    	question2.setAnswers(answers2);
    	
    	// Save question to quiz
    	Set<Question> questions = new HashSet<>();
    	questions.add(question1);
    	questions.add(question2);
    	quiz.setQuestions(questions);
    	
    	// Save quiz 
    	quizRepo.save(quiz);
    	
    	
	}

}
