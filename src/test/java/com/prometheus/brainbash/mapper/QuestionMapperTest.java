package com.prometheus.brainbash.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.dto.AnswerRequestDto;
import com.prometheus.brainbash.dto.QuestionDto;
import com.prometheus.brainbash.dto.QuestionRequestDto;
import com.prometheus.brainbash.model.Answer;
import com.prometheus.brainbash.model.Question;
import com.prometheus.brainbash.model.Quiz;

class QuestionMapperTest {

    @Test
    void shouldMapQuestionToQuestionDto() {
    	// Setup
        Quiz quiz = new Quiz();
        quiz.setId(1L);

        Question question = new Question();
        question.setId(100L);
        question.setText("Sample Question");
        question.setQuiz(quiz);

        Answer answer1 = new Answer();
        answer1.setId(10L);
        answer1.setText("Answer 1");
        answer1.setCorrect(true);
        answer1.setQuestion(question);

        Answer answer2 = new Answer();
        answer2.setId(11L);
        answer2.setText("Answer 2");
        answer2.setCorrect(false);
        answer2.setQuestion(question);

        Set<Answer> answers = new HashSet<>();
        answers.add(answer1);
        answers.add(answer2);
        question.setAnswers(answers);

        QuestionDto questionDto = new QuestionDto();

        // Call to method
        QuestionMapper.toQuestionDto(question, questionDto);

        // Assertions
        assertEquals(100L, questionDto.getId());
        assertEquals(1L, questionDto.getQuizId());
        assertEquals("Sample Question", questionDto.getText());
        assertEquals(2, questionDto.getAnswerDtos().size());
        
        Set<String> answerTexts = questionDto.getAnswerDtos().stream()
                .map(AnswerDto::getText)
                .collect(Collectors.toSet());
        assertTrue(answerTexts.contains("Answer 1"));
        assertTrue(answerTexts.contains("Answer 2"));
    }

    @Test
    void shouldMapQuestionRequestDtoToQuestion() {
        // Setup
        Quiz quiz = new Quiz();
        quiz.setId(2L);

        QuestionRequestDto questionRequestDto = new QuestionRequestDto();
        questionRequestDto.setText("New Question");

        AnswerRequestDto answerReq1 = new AnswerRequestDto();
        answerReq1.setText("New Answer 1");
        answerReq1.setCorrect(true);

        AnswerRequestDto answerReq2 = new AnswerRequestDto();
        answerReq2.setText("New Answer 2");
        answerReq2.setCorrect(false);

        Set<AnswerRequestDto> answerRequestDtos = new HashSet<>();
        answerRequestDtos.add(answerReq1);
        answerRequestDtos.add(answerReq2);
        questionRequestDto.setAnswerRequestDtos(answerRequestDtos);

        Question question = new Question();
        
        // Call to method
        QuestionMapper.toQuestion(questionRequestDto, quiz, question);

        // Assertions
        assertEquals("New Question", question.getText());
        assertEquals(2L, question.getQuiz().getId());
        assertEquals(2, question.getAnswers().size());

        Set<String> answerTexts = question.getAnswers().stream()
                .map(Answer::getText)
                .collect(Collectors.toSet());
        assertTrue(answerTexts.contains("New Answer 1"));
        assertTrue(answerTexts.contains("New Answer 2"));
    }
}
