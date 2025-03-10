package com.prometheus.brainbash.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prometheus.brainbash.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
