package com.prometheus.brainbash.dto;

import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.Category;
import com.prometheus.brainbash.model.DifficultyRating;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizCreationDto {
	
	@NotEmpty(message = "Title cannot be empty.")
	private String title;
	
	@NotEmpty(message = "Description must not empty.")
	private String description;
	
	@NotEmpty(message = "Image must be supplied")
	private String image;
	
	@NotNull(message = "Age Rating must be supplied.")
	private AgeRating ageRating;
	
	@NotNull(message = "Difficulty Rating must be supplied.")
	private DifficultyRating difficultyRating;
	
	@NotNull(message = "Category must be supplied")
	private Category category;
}
