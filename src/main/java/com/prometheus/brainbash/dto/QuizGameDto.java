package com.prometheus.brainbash.dto;

import java.util.Set;

import com.prometheus.brainbash.model.AgeRating;
import com.prometheus.brainbash.model.Category;
import com.prometheus.brainbash.model.DifficultyRating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizGameDto {
	private Long id;
	
	private String title;
	    
    private String description;
    
    private String image;
    
    private Set<Long> questionIds;
    
    private AgeRating ageRating;
    
    private DifficultyRating difficultyRating;
    
    private Category category;
}
