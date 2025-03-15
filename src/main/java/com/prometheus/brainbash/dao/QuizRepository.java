package com.prometheus.brainbash.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.DifficultyRating;
import com.prometheus.brainbash.model.Quiz;
import com.prometheus.brainbash.model.User;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
	List<Quiz> findByCreator_Username(String username);
	
	@Query("SELECT q FROM Quiz q WHERE "
			   + "(:middleTitle IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :middleTitle, '%')))"
		       + " AND (:difficultyRating IS NULL OR q.difficultyRating = :difficultyRating)"
		       + " AND (:ageRating IS NULL OR q.ageRating = :ageRating)")
	List<Quiz> findBySearch(
	        @Param("middleTitle") String middleTitle,
	        @Param("difficultyRating") DifficultyRating difficultyRating,
	        @Param("ageRating") AgeRating ageRating);
	
	@Query("SELECT q FROM Quiz q WHERE "
		       + "q.creator = :user "
			   + "AND (:middleTitle IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :middleTitle, '%')))"
		       + "AND (:difficultyRating IS NULL OR q.difficultyRating = :difficultyRating)"
		       + "AND (:ageRating IS NULL OR q.ageRating = :ageRating)")
	List<Quiz> findMineBySearch(
			@Param("user") User user,
	        @Param("middleTitle") String middleTitle,
	        @Param("difficultyRating") DifficultyRating difficultyRating,
	        @Param("ageRating") AgeRating ageRating);


}
