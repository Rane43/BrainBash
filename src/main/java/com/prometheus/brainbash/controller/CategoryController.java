package com.prometheus.brainbash.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.brainbash.model.Category;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	@GetMapping
	@PreAuthorize("hasRole('QUIZZER') or hasRole('QUIZ_DESIGNER')")
	public List<Category> getAllCategories() {
		return List.of(Category.values());
	}
}
