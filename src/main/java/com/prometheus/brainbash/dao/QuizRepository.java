package com.prometheus.brainbash.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prometheus.brainbash.model.Quiz;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
	List<Quiz> findByCreator_Username(String username);
}
