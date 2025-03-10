package com.prometheus.brainbash.dto;

import com.prometheus.brainbash.model.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizSummaryDto {
	private Long id;
	
	private String title;
	    
    private String description;
    
    private String image;
    
    private Category category;
}
