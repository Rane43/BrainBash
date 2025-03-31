package com.prometheus.brainbash.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.prometheus.brainbash.dto.QuizCreationDto;
import com.prometheus.brainbash.dto.QuizGameDto;
import com.prometheus.brainbash.dto.QuizSummaryDto;
import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.Category;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;

class QuizMapperTest {

    @Test
    void shouldMapQuizToQuizGameDto() {
        // Setup
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Science Quiz");
        quiz.setDescription("A fun science quiz");
        quiz.setImage("quiz_image.png");
        quiz.setAgeRating(AgeRating.ADULT);
        quiz.setDifficultyRating(DifficultyRating.EASY);
        quiz.setCategory(Category.ANATOMY);

        Question question1 = new Question();
        question1.setId(101L);
        Question question2 = new Question();
        question2.setId(102L);

        Set<Question> questions = new HashSet<>();
        questions.add(question1);
        questions.add(question2);
        quiz.setQuestions(questions);

        QuizGameDto quizGameDto = new QuizGameDto();

        // Call to method
        QuizMapper.toQuizGameDto(quiz, quizGameDto);

        // Assertions
        assertEquals(1L, quizGameDto.getId());
        assertEquals("Science Quiz", quizGameDto.getTitle());
        assertEquals("A fun science quiz", quizGameDto.getDescription());
        assertEquals("quiz_image.png", quizGameDto.getImage());
        assertEquals(AgeRating.ADULT, quizGameDto.getAgeRating());
        assertEquals(DifficultyRating.EASY, quizGameDto.getDifficultyRating());
        assertEquals(Category.ANATOMY, quizGameDto.getCategory());
        assertEquals(2, quizGameDto.getQuestionIds().size());
        assertTrue(quizGameDto.getQuestionIds().contains(101L));
        assertTrue(quizGameDto.getQuestionIds().contains(102L));
    }

    @Test
    void shouldMapQuizToQuizSummaryDto() {
        // Setup
        Quiz quiz = new Quiz();
        quiz.setId(2L);
        quiz.setTitle("Math Quiz");
        quiz.setDescription("A challenging math quiz");
        quiz.setImage("math_image.png");
        quiz.setCategory(Category.ANATOMY);

        QuizSummaryDto quizSummaryDto = new QuizSummaryDto();

        // Call to method
        QuizMapper.toQuizSummaryDto(quiz, quizSummaryDto);

        // Assertions
        assertEquals(2L, quizSummaryDto.getId());
        assertEquals("Math Quiz", quizSummaryDto.getTitle());
        assertEquals("A challenging math quiz", quizSummaryDto.getDescription());
        assertEquals("math_image.png", quizSummaryDto.getImage());
        assertEquals(Category.ANATOMY, quizSummaryDto.getCategory());
    }

    @Test
    void shouldMapQuizCreationDtoToQuiz() {
        // Setup
        User creator = new User();
        creator.setId(10L);

        QuizCreationDto quizCreationDto = new QuizCreationDto();
        quizCreationDto.setTitle("History Quiz");
        quizCreationDto.setDescription("A fun history quiz");
        quizCreationDto.setImage("history_image.png");
        quizCreationDto.setAgeRating(AgeRating.ADULT);
        quizCreationDto.setDifficultyRating(DifficultyRating.EASY);
        quizCreationDto.setCategory(Category.ANATOMY);

        Quiz quiz = new Quiz();

        // Call to method
        QuizMapper.quizCreationDtoToQuiz(quizCreationDto, creator, quiz);

        // Assertions
        assertEquals("History Quiz", quiz.getTitle());
        assertEquals("A fun history quiz", quiz.getDescription());
        assertEquals("history_image.png", quiz.getImage());
        assertEquals(AgeRating.ADULT, quiz.getAgeRating());
        assertEquals(DifficultyRating.EASY, quiz.getDifficultyRating());
        assertEquals(Category.ANATOMY, quiz.getCategory());
        assertEquals(creator, quiz.getCreator());
        assertEquals(1, quiz.getDevelopers().size());
        assertTrue(quiz.getDevelopers().contains(creator));
        assertNotNull(quiz.getQuestions());
        assertTrue(quiz.getQuestions().isEmpty());
    }
}
