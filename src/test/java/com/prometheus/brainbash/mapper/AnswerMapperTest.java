package com.prometheus.brainbash.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.prometheus.brainbash.dto.AnswerDto;
import com.prometheus.brainbash.dto.AnswerRequestDto;
import com.prometheus.brainbash.model.Answer;
import com.prometheus.brainbash.model.Question;

class AnswerMapperTest {

    @Test
    void shouldMapAnswerToAnswerDto() {
        Question question = new Question();
        question.setId(1L);

        Answer answer = new Answer();
        answer.setId(100L);
        answer.setText("Sample Answer");
        answer.setCorrect(true);
        answer.setQuestion(question);

        AnswerDto answerDto = new AnswerDto();

        AnswerMapper.toAnswerDto(answer, answerDto);

        assertEquals(100L, answerDto.getId());
        assertEquals("Sample Answer", answerDto.getText());
        assertTrue(answerDto.isCorrect());
        assertEquals(1L, answerDto.getQuestion_id());
    }

    @Test
    void shouldMapAnswerDtoToAnswer() {
        Question question = new Question();
        question.setId(1L);

        AnswerDto answerDto = new AnswerDto();
        answerDto.setText("Updated Answer");
        answerDto.setCorrect(false);

        Answer answer = new Answer();

        AnswerMapper.toAnswer(answerDto, question, answer);

        assertEquals("Updated Answer", answer.getText());
        assertFalse(answer.isCorrect());
        assertEquals(1L, answer.getQuestion().getId());
    }

    @Test
    void shouldMapAnswerRequestDtoToAnswer() {
        Question question = new Question();
        question.setId(2L);

        AnswerRequestDto answerRequestDto = new AnswerRequestDto();
        answerRequestDto.setText("Another Answer");
        answerRequestDto.setCorrect(true);

        Answer answer = new Answer();

        AnswerMapper.toAnswer(answerRequestDto, question, answer);

        assertEquals("Another Answer", answer.getText());
        assertTrue(answer.isCorrect());
        assertEquals(2L, answer.getQuestion().getId());
    }
}

