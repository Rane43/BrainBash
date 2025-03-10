package com.prometheus.brainbash.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;
import com.prometheus.brainbash.model.UserQuizScore;

public interface UserQuizScoreRepository extends JpaRepository<UserQuizScore, Long>{
	UserQuizScore findByUserAndQuiz(User user, Quiz quiz);
}
