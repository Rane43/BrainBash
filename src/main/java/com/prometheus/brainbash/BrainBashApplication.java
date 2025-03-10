package com.prometheus.brainbash;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prometheus.brainbash.dao.QuizRepository;
import com.prometheus.brainbash.dao.UserRepository;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.Answer;
import com.prometheus.brainbash.model.Category;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;

@SpringBootApplication
public class BrainBashApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrainBashApplication.class, args);
	}
	
	
	// Temp user created
	/*
	@Bean
    CommandLineRunner init(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create a test admin user on program startup (if not exists)
        	User user = new User();
        	user.setUsername("admin".toLowerCase());
        	user.setPassword(passwordEncoder.encode("Password123!"));
        	user.setAuthorities(Set.of(Role.ROLE_ADMIN));
        	userRepo.save(user);
            System.out.println("User saved successfully!");
        };
    }
    */
	
	// Temp Quiz created
	@Bean
    CommandLineRunner init(UserRepository userRepo, PasswordEncoder passwordEncoder, QuizRepository quizRepo) {
        return args -> {
        	// User to create quiz
        	User user = new User();
        	user.setUsername("Rane43");
        	user.setPassword(passwordEncoder.encode("Password123!"));
        	
        	// Quiz itself
        	Quiz quiz = new Quiz();
        	quiz.setTitle("Geography Quiz");
        	quiz.setDescription("Easy teen geography quiz");
        	quiz.setAgeRating(AgeRating.TEEN);
        	quiz.setCategory(Category.GEOGRAPHY);
        	quiz.setDifficultyRating(DifficultyRating.EASY);
        	// Add user as creator and as one of the developers
        	quiz.setCreator(user);
        	quiz.setDevelopers(Set.of(user));
        	quiz.setImage("question-image-test.jpeg");
        	quiz.setPublished(false);
        	
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
        	answer21.setText("5");
        	
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
        	
        	// Save user
        	userRepo.save(user);
        	
        	// Save quiz 
        	quizRepo.save(quiz);
            System.out.println("Quiz saved successfully!");
        };
    }

}
