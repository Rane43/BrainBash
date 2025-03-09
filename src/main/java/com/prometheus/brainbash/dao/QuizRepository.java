package com.prometheus.brainbash.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prometheus.brainbash.model.Quiz;


public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
